package com.kg.platform.online_course.api_controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/")
    public String getHelloWorld(){
        return "Hello world!";
    }
}
