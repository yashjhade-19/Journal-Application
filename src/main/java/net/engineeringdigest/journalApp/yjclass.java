package net.engineeringdigest.journalApp;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class yjclass {

    @GetMapping("/aot")
    public String method()
    {
        return "eren yeager";
    }
}
