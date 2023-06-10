package com.kg.platform.online_course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@EnableAsync
@Controller
public class OnlineCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCourseApplication.class, args);
        System.out.println("Welcome to this project!");
    }

    @GetMapping("/")
    public String index() {
        return "welcome";
    }

}
