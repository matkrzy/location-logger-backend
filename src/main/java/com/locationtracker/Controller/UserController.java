package com.locationtracker.Controller;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserController.class, args);
    }
}
