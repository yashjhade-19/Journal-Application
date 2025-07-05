package com.yashjhade.journalApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
public class myclass  {

    @GetMapping("/abc")  //api name
    public String sayHello(){
        return "Hello0oooooooooo";
    }

}
