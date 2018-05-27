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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path = "/device")
public class DeviceController {
    @Autowired
    public DeviceRepository deviceRepository;

    @Autowired
    public UserRepository userRepository;


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity addDevice(@RequestBody Device device, final BindingResult bindingResult) {
        JsonResponse response = new JsonResponse();

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }

        Device dev = this.deviceRepository.save(new Device(device.getName(), device.getUserId()));

        if (dev != null) {
            response.setMessageError("An error occurred");
            return response.getResponseAsResponseEntity();
        }else{
            return new ResponseEntity(dev,HttpStatus.CREATED);
        }
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
    ResponseEntity<?> updateDevice(@RequestBody Device device, @PathVariable int id, Authentication auth, final BindingResult bindingResult) {
        JsonResponse response = new JsonResponse();

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        Device updatedDevice = deviceRepository.findById(id);


        if(device.getId() != id){
            response.setMessageError("Device id mismatch");
            return response.getResponseAsResponseEntity();
        }

        if (updatedDevice.getUserId() == user.getId()) {
            updatedDevice.setName(device.getName());
            Device dev = deviceRepository.save(updatedDevice);
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
            deviceRepository.save(device);
            response.setMessage("Device has been removed");

            return response.getResponseAsResponseEntity();
        } else {
            response.setMessageError("The device is not assigned to you");
            return response.getResponseAsResponseEntity();
        }

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DeviceController.class, args);
    }
}
