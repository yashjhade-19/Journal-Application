package com.yashjhade.journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/heath-theek")
    public String healthCheck(){
        return "OK";
    }
}
