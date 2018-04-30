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
import org.springframework.security.core.Authentication;
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


    @PostMapping(path = "/register", produces = "application/json; charset=utf-8")
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

    @GetMapping(path = "/me", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getLoggedUserDetails(Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            response.setStatus(HttpStatus.OK);
            response.addFieldtoResponse("username", user.getUsername());
            response.addFieldtoResponse("id", user.getId());
            response.addFieldtoResponse("removed", user.isRemoved());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Cannot identify user");
        }

        return response.getResponseAsResponseEntity();
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserController.class, args);
    }
}
