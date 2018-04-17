package com.locationtracker.controller;

import com.locationtracker.model.Track;
import com.locationtracker.model.User;
import com.locationtracker.repository.PointRepository;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.service.TracksService;
import com.locationtracker.utils.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.locationtracker.repository.TrackRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Controller
@RequestMapping(path = "/tracks")
public class TracksController {
    private Utils utils = new Utils();

    @Autowired
    public TrackRepository trackRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PointRepository pointRepository;

    @Autowired
    public TracksService tracksService;

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

    @Transactional
    @PostMapping(path = "/import", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> importPointsFromFile(
            @RequestParam("file[0]") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("device") int deviceId,
            Authentication auth) {

        return this.tracksService.importTrackFromFile(file, name, deviceId, auth);

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TracksController.class, args);
    }
}
