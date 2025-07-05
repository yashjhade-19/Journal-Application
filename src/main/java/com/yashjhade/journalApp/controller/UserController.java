package com.yashjhade.journalApp.controller;


import com.yashjhade.journalApp.api.response.WeatherResponse;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.UserRepository;
import com.yashjhade.journalApp.service.UserService;
import com.yashjhade.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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
    public ResponseEntity<?> deleteUserBYId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    userRepository.deleteByUserName(authentication.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

@GetMapping
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
