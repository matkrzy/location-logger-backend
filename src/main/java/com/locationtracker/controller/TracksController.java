package com.locationtracker.controller;

import com.locationtracker.model.Track;
import com.locationtracker.model.User;
import com.locationtracker.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.locationtracker.repository.TrackRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/tracks")
public class TracksController {

    @Autowired
    public TrackRepository trackRepository;

    @Autowired
    public UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody
    List<Track> getAllActiveTracks(Authentication auth) {

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        return trackRepository.findByRemovedIsFalseAndUserId(user.getId());
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<Track> getAllTracks() {

        return trackRepository.findAll();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TracksController.class, args);
    }
}
