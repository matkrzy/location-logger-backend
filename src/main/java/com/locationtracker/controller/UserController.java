package com.locationtracker.controller;

import com.locationtracker.model.User;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.utils.JsonResponse;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping(path = "/sign-up", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<String> signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        JsonResponse response = new JsonResponse();

        User userHelper = null;

        try {
            userHelper = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            response.setMessageError("User is already exist. Please provide another username");
        }

        if (userHelper != null) {
            response.setStatus(HttpStatus.CREATED);
            response.setMessage("User created");
        }

        return new ResponseEntity(response.getMessageAsString(), response.getStatus());
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserController.class, args);
    }
}
