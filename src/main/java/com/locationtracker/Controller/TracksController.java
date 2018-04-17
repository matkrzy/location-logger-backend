package com.locationtracker.Controller;

import com.locationtracker.Model.Track;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.locationtracker.Repository.TrackRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/tracks")
public class TracksController {

    @Autowired
    public TrackRepository trackRepository;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Track> getAllActiveTracks() {

        return trackRepository.findByRemovedIsFalse();
    }

    @GetMapping(path ="/all")
    public @ResponseBody
    List<Track> getAllTracks() {

        return trackRepository.findAll();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TracksController.class, args);
    }
}
