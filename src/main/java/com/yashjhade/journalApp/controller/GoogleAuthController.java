package com.yashjhade.journalApp.controller;

import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.UserRepository;
import com.yashjhade.journalApp.utilis.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Get Google OAuth2 URL")
    @GetMapping("/url")
    public ResponseEntity<String> getGoogleAuthUrl() {
        String authUrl = "https://accounts.google.com/o/oauth2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=email%20profile" +
                "&access_type=offline" +
                "&prompt=consent";

        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    @Transactional
    public void handleGoogleCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        String redirect;
        try {
            // Step 1: Exchange code for token
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            if (tokenResponse.getStatusCode() != HttpStatus.OK || tokenResponse.getBody() == null) {
                redirect = frontendUrl + "/login?error=token_failed";
                response.sendRedirect(redirect);
                return;
            }

            String idToken = (String) tokenResponse.getBody().get("id_token");
            if (idToken == null) {
                redirect = frontendUrl + "/login?error=missing_id_token";
                response.sendRedirect(redirect);
                return;
            }

            // Step 2: Get user info
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            if (userInfoResponse.getStatusCode() != HttpStatus.OK || userInfoResponse.getBody() == null) {
                redirect = frontendUrl + "/login?error=userinfo_failed";
                response.sendRedirect(redirect);
                return;
            }

            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            if (email == null) {
                redirect = frontendUrl + "/login?error=missing_email";
                response.sendRedirect(redirect);
                return;
            }

            // Step 3: Check or create user
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));
            User user = optionalUser.orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUserName(name != null ? name : email.split("@")[0]);
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                newUser.setRoles(Collections.singletonList("USER"));
                return userRepository.save(newUser);
            });

            // Step 4: Generate JWT
            String jwtToken = jwtUtil.generateToken(user.getUserName());
            log.info("Generated token for {}: {}", email, jwtToken);

            // Step 5: Redirect to frontend
            String frontendRedirectUrl = frontendUrl + "/auth-handler"
                    + "?token=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8)
                    + "&username=" + URLEncoder.encode(user.getUserName(), StandardCharsets.UTF_8)
                    + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);

            log.info("Redirecting to frontend with: {}", frontendRedirectUrl);
            response.sendRedirect(frontendRedirectUrl);

        } catch (DataIntegrityViolationException e) {
            log.warn("Data integrity violation during user creation (likely duplicate)", e);
            response.sendRedirect(frontendUrl + "/login?error=duplicate_user");
        } catch (Exception e) {
            log.error("Unexpected error in Google callback handler", e);
            response.sendRedirect(frontendUrl + "/login?error=server_error");
        }
    }
}
