package com.locationtracker.Controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationtracker.Model.Point;
import com.locationtracker.Model.Track;
import com.locationtracker.Repository.PointRepository;
import com.locationtracker.utils.Utils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.locationtracker.Repository.TrackRepository;
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
    Track deleteTrack(@PathVariable int id) {
        Track track = trackRepository.findById(id);
        track.setRemoved(true);
        return trackRepository.save(track);
    }

    @GetMapping(path = "/{id}/points")
    public @ResponseBody
    List<Point> getTrackPoints(@PathVariable int id) {

        return pointRepository.findAllByTrackId(id);
    }

    @PostMapping(path = "/{id}/points")
    public @ResponseBody
    void addPointToTrack(@PathVariable int id, @RequestBody Point point) {
        point.setTrackId(id);
        pointRepository.save(point);
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
