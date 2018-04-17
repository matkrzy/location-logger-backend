package com.locationtracker.Controller;

import com.locationtracker.Model.Track;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.locationtracker.Repository.TrackRepository;

@Controller
@RequestMapping(path = "/track")
public class TrackController {
    @Autowired
    public TrackRepository trackRepository;

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Track getTrackById(@PathVariable int id) {

        return trackRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Track addTrack(@RequestBody Track track) {

        return trackRepository.save(track);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody
    Track updateTrack(@RequestBody Track device) {

        return trackRepository.save(device);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    Track deleteTrack(@PathVariable int id){
        Track track = trackRepository.findById(id);
        track.setRemoved(true);
        return trackRepository.save(track);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(TrackController.class, args);
    }
}
