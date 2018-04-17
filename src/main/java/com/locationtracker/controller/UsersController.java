package com.locationtracker.controller;

import com.locationtracker.enums.UserRole;
import com.locationtracker.model.User;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.utils.JsonResponse;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Role;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping(path = "/users")
@Secured("ADMIN")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getAllUsersByRole(Authentication auth) {
        JsonResponse response = new JsonResponse();

        List<User> users = userRepository.findAllByRole(UserRole.USER);

        if (users != null) {
            return new ResponseEntity(users, HttpStatus.OK);
        } else {
            response.setMessageError("Cannot identify user");
        }

        return response.getResponseAsResponseEntity();
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(UsersController.class, args);
    }
}
