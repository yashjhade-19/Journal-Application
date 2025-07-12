package com.yashjhade.journalApp.controller;


import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    @Operation(summary="Get all user ")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/create-admin-user")
    @Operation(summary="Create Admin")
    public void createuser(@RequestBody User user){
        userService.saveAdmin(user);
    }
}
