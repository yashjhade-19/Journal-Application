package com.yashjhade.journalApp.controller;


import com.yashjhade.journalApp.api.response.WeatherResponse;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.UserRepository;
import com.yashjhade.journalApp.service.UserService;
import com.yashjhade.journalApp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs", description = "Read, Update & Delete User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/test")
    public String testPublic() {
        return "This is  verified User!";
    }

@PutMapping
@Operation(summary="Update user by id")
    public ResponseEntity<?> updateUser(@RequestBody User user){
    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    String userName= authentication.getName();

User userIndb=userService.findByUserName(userName);
if(userIndb !=null){

    userIndb.setUserName(user.getUserName());
    userIndb.setPassword(user.getPassword());
userService.saveNewUser(userIndb);
}
return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

@DeleteMapping
@Operation(summary="Delete user by id")
    public ResponseEntity<?> deleteUserBYId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    userRepository.deleteByUserName(authentication.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

@GetMapping
@Operation(summary="Greet user weather API")
    public ResponseEntity<?> greeting(){
    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
     WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
     String greeting="";
     if(weatherResponse!=null){
         greeting= " ,Weather feels like " +weatherResponse.getCurrent().getFeelslike();
     }
    return new ResponseEntity<>("hi " + authentication.getName() + greeting,HttpStatus.OK);

}
}
