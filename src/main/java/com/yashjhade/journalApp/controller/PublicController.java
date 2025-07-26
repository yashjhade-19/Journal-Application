package com.yashjhade.journalApp.controller;


import com.yashjhade.journalApp.dto.UserDTO;
import com.yashjhade.journalApp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.UserRepositoryImpl;
import com.yashjhade.journalApp.service.UserService;
import com.yashjhade.journalApp.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String testPublic() {
        return "This is public!";
    }


    @PostMapping("/signup")
    @Operation(summary="Signup user")
    public ResponseEntity<String> signup(@RequestBody UserDTO user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User with this email already exists");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());

        userService.saveNewUser(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }


    @PostMapping("/login")
    @Operation(summary="Login User")
    public ResponseEntity<String> loginuser(@RequestBody UserDTO user) {
//        User newUser = new User();
//        newUser.setUserName(user.getUserName());
//        newUser.setPassword(user.getPassword());
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/check-email")
//    public void checkGmail(@RequestBody User user){
//
//        userRepositoryImpl.getUserForSA(user);
//    }
}
