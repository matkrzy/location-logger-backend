package com.locationtracker.controller;

import com.locationtracker.model.User;
import com.locationtracker.repository.UserRepository;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        JSONObject response = new JSONObject();
        try {
            response.append("message", "user created");
            response.append("status", HttpStatus.OK);
        }catch(Exception e){
            System.out.print(e.toString());
        }

        return new ResponseEntity(response.toString(), HttpStatus.OK);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserController.class, args);
    }
}
