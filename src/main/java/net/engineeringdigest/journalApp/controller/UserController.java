package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
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

}
