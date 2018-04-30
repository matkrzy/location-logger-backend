package com.locationtracker.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationtracker.model.Point;
import com.locationtracker.model.Track;
import com.locationtracker.model.User;
import com.locationtracker.repository.PointRepository;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.utils.JsonResponse;
import com.locationtracker.utils.Utils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.locationtracker.repository.TrackRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping(path = "/track")
public class TrackController {
    @Autowired
    public TrackRepository trackRepository;

    @Autowired
    public PointRepository pointRepository;

    @Autowired
    public UserRepository userRepository;

    @GetMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getTrackById(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Track track = trackRepository.findById(id);

        if (track.getUserId() == user.getId()) {
            return new ResponseEntity(track, HttpStatus.OK);
        } else {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> addTrack(@RequestBody Track track, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        if (track.getUserId() == user.getId()) {
            Track saved = trackRepository.save(track);
            return new ResponseEntity(saved, HttpStatus.OK);

        } else {
            response.setMessageError("User id mismatch!");
            return response.getResponseAsResponseEntity();
        }
    }

    @PutMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> updateTrack(@RequestBody Track track, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        if (track.getUserId() == user.getId()) {
            Track saved = trackRepository.save(track);
            return new ResponseEntity(saved, HttpStatus.OK);

        } else {
            response.setMessageError("User id mismatch!");
            return response.getResponseAsResponseEntity();
        }
    }

    @DeleteMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> deleteTrack(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Track track = trackRepository.findById(id);

        if (track.getUserId() == user.getId()) {
            track.setRemoved(true);
            track = trackRepository.save(track);
            return new ResponseEntity(track, HttpStatus.OK);
        } else {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        }
    }

    @GetMapping(path = "/{id}/points", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getTrackPoints(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        Track track = trackRepository.findById(id);

        if (track.getUserId() == user.getId()) {
            List<Point> points = pointRepository.findAllByTrackId(id);

            return new ResponseEntity(points, HttpStatus.OK);
        } else {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        }
    }

    @PostMapping(path = "/{id}/points", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> addPointToTrack(@PathVariable int id, @RequestBody Point point, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        Track track = trackRepository.findById(id);

        if (track.getUserId() != user.getId()) {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        } else if (track.isRemoved()) {
            response.setMessageError("Track is removed");
            return response.getResponseAsResponseEntity();
        } else {
            point.setTrackId(id);
            pointRepository.save(point);
            return new ResponseEntity(null, HttpStatus.OK);
        }

    }

    @PostMapping(path = "/{trackId}/import")
    public @ResponseBody
    List<Point> importPointsFromFile(@PathVariable int trackId, @RequestParam("file") MultipartFile file) {
        Utils utils = new Utils();

        try {
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());

            String text = null;
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                text = scanner.useDelimiter("\\A").next();
            }

            JSONObject jsonObj = new JSONObject(text);
            JSONArray pointsArray = jsonObj.getJSONArray("points");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


            Point prevPoint = null;
            for (int i = 0; i < pointsArray.length(); i++) {
                JSONObject point = pointsArray.getJSONObject(i);

                Point mappedPoint = objectMapper.readValue(point.toString(), Point.class);
                mappedPoint.setTrackId(trackId);
                mappedPoint.setDuration(utils.getTimeDiffInMinutes(mappedPoint, mappedPoint));

                if (i > 0) {
                    Double distance = utils.getDistance(prevPoint, mappedPoint);
                    mappedPoint.setDistance(distance);
                    mappedPoint.setDuration(utils.getTimeDiffInMinutes(prevPoint, mappedPoint));
                }

                pointRepository.save(mappedPoint);

                prevPoint = mappedPoint;
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }

        List<Point> trackPoints = pointRepository.findAllByTrackId(trackId);
        Track track = trackRepository.findById(trackId);

        Point startPoint = trackPoints.get(0);
        Point endPoint = trackPoints.get(trackPoints.size() - 1);

        Point maxSpeed = Collections.max(trackPoints, Comparator.comparingDouble(Point::getSpeed));
        Double minSpeed = trackPoints.stream().filter(p -> p.getSpeed() > 0.0).mapToDouble(p -> p.getSpeed()).min().orElse(0);


        Double avgSpeed = trackPoints.stream()
                .mapToDouble(p -> p.getSpeed())
                .average()
                .orElse(0);

        Point maxAltitude = Collections.max(trackPoints, Comparator.comparingDouble(Point::getAltitude));
        Double minAltitude = trackPoints.stream().filter(p -> p.getAltitude() > 0.0).mapToDouble(p -> p.getAltitude()).min().orElse(0);

        Double avgAltitude = trackPoints.stream()
                .mapToDouble(p -> p.getAltitude())
                .average()
                .orElse(0);

        Double distance = trackPoints.stream().mapToDouble(Point::getDistance).sum();


        track.setDate(startPoint.getTimestamp());
        track.setStartTime(startPoint.getTimestamp());
        track.setEndTime(endPoint.getTimestamp());

        track.setMinSpeed(minSpeed);
        track.setMaxSpeed(maxSpeed.getSpeed());
        track.setAvgSpeed(avgSpeed);

        track.setMinAltitude(minAltitude);
        track.setMaxAltitude(maxAltitude.getAltitude());
        track.setAvgAltitude(avgAltitude);

        track.setDistance(distance);

        track.setDuration(utils.getTimeDiffInMinutes(startPoint, endPoint));
        trackRepository.save(track);

        return trackPoints;
    }

    @GetMapping(path = "/{id}/navigate")
    public @ResponseBody
    Point getNavigationPoint(@PathVariable int id) {
        List<Point> points = pointRepository.findAllByTrackId(id);

        return points.get(points.size() - 1);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(TrackController.class, args);
    }
}
