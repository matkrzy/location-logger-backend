package com.locationtracker.controller;

import com.locationtracker.model.Device;
import com.locationtracker.model.User;
import com.locationtracker.repository.DeviceRepository;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(path = "/device")
public class DeviceController {
    @Autowired
    public DeviceRepository deviceRepository;

    @Autowired
    public UserRepository userRepository;


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Device addDevice(@RequestBody Device device) {

        return this.deviceRepository.save(new Device(device.getName(), device.getUserId()));
    }

    @GetMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getDevice(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Device device = deviceRepository.findById(id);

        if (device.getUserId() == user.getId()) {
            return new ResponseEntity(device, HttpStatus.OK);
        } else {
            response.setMessageError("The device is not assigned to you");
            return response.getResponseAsResponseEntity();
        }

    }

    @PutMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> updateDevice(@RequestBody Device device, Authentication auth) {

        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        Device checkDevice = deviceRepository.findById(device.getId());


        if (checkDevice.getUserId() == user.getId()) {
            Device dev = deviceRepository.save(device);
            return new ResponseEntity(dev, HttpStatus.OK);
        } else {
            response.setMessageError("The device is not assigned to you");
            return response.getResponseAsResponseEntity();
        }

    }

    @DeleteMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> deleteDevice(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        Device device = deviceRepository.findById(id);

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);


        if (device.getUserId() == user.getId()) {
            device.setRemoved(true);
            device = deviceRepository.save(device);

            return new ResponseEntity(device, HttpStatus.OK);
        } else {
            response.setMessageError("The device is not assigned to you");
            return response.getResponseAsResponseEntity();
        }

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DeviceController.class, args);
    }
}
