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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping(path = "/tracks")
public class TracksController {

    @Autowired
    public TrackRepository trackRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PointRepository pointRepository;

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

    @PostMapping(path = "/import", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> importPointsFromFile(
            @RequestParam("file[0]") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("device") int deviceId,
            Authentication auth) {
        Utils utils = new Utils();
        JsonResponse response = new JsonResponse();


        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);


        Track track = new Track();
        track.setName(name);
        track.setRemoved(false);
        track.setDate(new Date());
        track.setDeviceId(deviceId);
        track.setUserId(user.getId());

        track = trackRepository.save(track);

        if (track == null) {
            response.setMessageError("Error during track creation");
        }


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
            Point firstPoint = null;
            for (int i = 0; i < pointsArray.length(); i++) {
                JSONObject point = pointsArray.getJSONObject(i);

                Point mappedPoint = objectMapper.readValue(point.toString(), Point.class);
                mappedPoint.setTrackId(track.getId());
                mappedPoint.setDuration(utils.getTimeDiffInMinutes(mappedPoint, mappedPoint));

                if (i > 0) {
                    Double distance = utils.getDistance(prevPoint, mappedPoint);
                    mappedPoint.setDistance(distance);
                    mappedPoint.setDuration(utils.getTimeDiffInMinutes(firstPoint, mappedPoint));
                } else {
                    mappedPoint.setDistance(0.0);
                    firstPoint = mappedPoint;
                }

                pointRepository.save(mappedPoint);

                prevPoint = mappedPoint;
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }

        List<Point> trackPoints = pointRepository.findAllByTrackId(track.getId());

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
        track = trackRepository.save(track);

        if (track != null) {
            response.setMessage(track.toString());
            return new ResponseEntity(track, HttpStatus.OK);
        } else {
            response.setMessageError("Something went wrong");
            return response.getResponseAsResponseEntity();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TracksController.class, args);
    }
}
