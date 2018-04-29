package com.locationtracker.controller;

import com.locationtracker.model.Device;
import com.locationtracker.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path ="/device")
public class DeviceController {
    @Autowired
    public DeviceRepository deviceRepository;


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Device addDevice(@RequestBody Device device) {

        return this.deviceRepository.save(new Device(device.getName(), device.getUserId()));
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Device getDevice(@PathVariable int id) {

        return deviceRepository.findById(id);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody
    Device updateDevice(@RequestBody Device device) {

        return deviceRepository.save(device);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    Device deleteDevice(@PathVariable int id){
        Device device = deviceRepository.findById(id);
        device.setRemoved(true);
        return deviceRepository.save(device);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DeviceController.class, args);
    }
}
