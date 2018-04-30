package com.locationtracker.controller;

import com.locationtracker.model.Device;
import com.locationtracker.model.User;
import com.locationtracker.repository.DeviceRepository;
import com.locationtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path ="/devices")
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

    @GetMapping(path = "/all")
    public @ResponseBody
    List<Device> getAllDevices() {

        return deviceRepository.findAll();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DevicesController.class, args);
    }
}
