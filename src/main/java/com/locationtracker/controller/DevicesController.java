package com.locationtracker.controller;

import com.locationtracker.model.Device;
import com.locationtracker.model.User;
import com.locationtracker.repository.DeviceRepository;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.utils.JsonResponse;
import com.locationtracker.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/devices")
public class DevicesController {
    @Autowired
    public DeviceRepository deviceRepository;

    @Autowired
    public UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Device> getAllActiveDevices(Authentication auth) {
        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        return deviceRepository.findByRemovedIsFalseAndUserId(user.getId());
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<?> addDevice(@RequestBody Device device, final BindingResult bindingResult, Authentication auth) {
        JsonResponse response = new JsonResponse();

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        device.setUserId(user.getId());
        device.setUuid(UUID.randomUUID().toString());
        Device saved = deviceRepository.save(device);

        if (saved != null) {
            return new ResponseEntity(saved, HttpStatus.OK);
        } else {
            response.setMessageError("An error occurred");
            return response.getResponseAsResponseEntity();
        }
    }

    //TODO fix when roles will be available
    //
//    @GetMapping(path = "/all")
//    public @ResponseBody
//    List<Device> getAllDevices() {
//
//        return deviceRepository.findAll();
//    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DevicesController.class, args);
    }
}
