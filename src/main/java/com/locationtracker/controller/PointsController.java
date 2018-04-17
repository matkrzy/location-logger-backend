package com.locationtracker.controller;

import com.locationtracker.model.Point;
import com.locationtracker.model.Track;
import com.locationtracker.model.User;
import com.locationtracker.repository.PointRepository;
import com.locationtracker.repository.TrackRepository;
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
@RequestMapping(path = "/points")
public class PointsController {
    @Autowired
    public PointRepository pointRepository;

    @Autowired
    public TrackRepository trackRepository;

    @Autowired
    public UserRepository userRepository;

    @GetMapping(path = "/{id}")
    public @ResponseBody
    ResponseEntity getPointsByTrackId(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Track track = trackRepository.findById(id);

        if (track == null) {
            response.setMessageError("An error occurred");
            return response.getResponseAsResponseEntity();
        }

        if (track.getUserId() != user.getId()) {
            response.setMessageError("The track is not yours");
            return response.getResponseAsResponseEntity();

        } else {
            List<Point> points = pointRepository.findAllByTrackIdOrderByTimestampAsc(id);

            if (points == null) {
                response.setMessageError("An error occurred");
                return response.getResponseAsResponseEntity();
            }

            return new ResponseEntity(points, HttpStatus.OK);
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(PointsController.class, args);
    }
}
