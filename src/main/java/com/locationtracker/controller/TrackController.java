package com.locationtracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationtracker.model.Device;
import com.locationtracker.model.Point;
import com.locationtracker.model.Track;
import com.locationtracker.model.User;
import com.locationtracker.repository.DeviceRepository;
import com.locationtracker.repository.PointRepository;
import com.locationtracker.repository.UserRepository;
import com.locationtracker.service.TracksService;
import com.locationtracker.utils.JsonResponse;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.locationtracker.repository.TrackRepository;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequestMapping(path = "/track")
public class TrackController {
    @Autowired
    public TrackRepository trackRepository;

    @Autowired
    public PointRepository pointRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public DeviceRepository deviceRepository;

    @Autowired
    public TracksService tracksService;

    @GetMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getTrackDetails(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Track track = trackRepository.findById(id);
        List<Point> points = pointRepository.findAllByTrackIdOrderByTimestampAsc(id);
        ObjectMapper mapper = new ObjectMapper();

        if (track == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMessageError("Track doesn't exist");
            return response.getResponseAsResponseEntity();
        }

        if (track.getUserId() == user.getId()) {
            try {
                response.setStatus(HttpStatus.OK);
                String jsonInString = mapper.writeValueAsString(track);
                response.convertToObjectFromString(jsonInString);

                jsonInString = mapper.writeValueAsString(points);
                JSONArray jsonArr = new JSONArray(jsonInString);

                response.addFieldtoResponse("points", jsonArr);
            } catch (Exception e) {
                response.setMessageError("Something went wrong");
            }

            return response.getResponseAsResponseEntity();
        } else {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        }
    }

    @PostMapping(produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> addTrack(@RequestBody Track track, final BindingResult bindingResult, Authentication auth) {
        JsonResponse response = new JsonResponse();

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }


        Track saved = trackRepository.save(track);
        return new ResponseEntity(saved, HttpStatus.CREATED);

    }

    @Transactional
    @PutMapping(path = "/{id}", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> updateTrack(@RequestBody Track track, @PathVariable int id, final BindingResult bindingResult, Authentication auth) {
        JsonResponse response = new JsonResponse();

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Track compareTrack = trackRepository.findById(track.getId());

        if (track.getUserId() == user.getId() && track.getId() == id && compareTrack.getId() == id && compareTrack.getId() == track.getId()) {
            Track saved = trackRepository.save(track);
            return new ResponseEntity(saved, HttpStatus.OK);

        } else {
            response.setMessageError("Track details mismatch!");
            return response.getResponseAsResponseEntity();
        }
    }

    @Transactional
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
            pointRepository.removeAllByTrackId(track.getId());
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
            List<Point> points = pointRepository.findAllByTrackIdOrderByTimestampAsc(id);

            return new ResponseEntity(points, HttpStatus.OK);
        } else {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        }
    }

    @PostMapping(path = "/{id}/point", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> addPointToTrack(
            @RequestHeader("Device-uuid") String uuid,
            @PathVariable int id,
            @RequestBody Point point,
            final BindingResult bindingResult,
            Authentication auth) {
        JsonResponse response = new JsonResponse();

        Device device = deviceRepository.findByUuid(uuid);

        if(device == null){
            response.setMessageError("Device uuid mismatch");
            return response.getResponseAsResponseEntity();
        }

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        Track track = trackRepository.findById(id);

        if(track==null){
            response.setMessageError("Track does not exist");
            return response.getResponseAsResponseEntity();
        }

        if (track.getUserId() != user.getId()) {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        } else if (track.isRemoved()) {
            response.setMessageError("Track is removed");
            return response.getResponseAsResponseEntity();
        } else {
            point.setTrackId(id);
            pointRepository.save(point);
            return new ResponseEntity(null, HttpStatus.CREATED);
        }

    }

    @PostMapping(path = "/{id}/points", produces = "application/json; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> addPointsToTrack(
            @RequestHeader("Device-uuid") String uuid,
            @PathVariable int id,
            @RequestBody List<Point> points,
            final BindingResult bindingResult,
            Authentication auth) {
        JsonResponse response = new JsonResponse();

        Device device = deviceRepository.findByUuid(uuid);

        if(device == null){
            response.setMessageError("Device uuid mismatch");
            return response.getResponseAsResponseEntity();
        }

        if (bindingResult.hasErrors()) {
            response.setErrorsForm(bindingResult);

            return response.getResponseAsResponseEntity();
        }

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        Track track = trackRepository.findById(id);

        if(track==null){
            response.setMessageError("Track does not exist");
            return response.getResponseAsResponseEntity();
        }

        if (track.getUserId() != user.getId()) {
            response.setMessageError("You are not owner of this track");
            return response.getResponseAsResponseEntity();
        } else if (track.isRemoved()) {
            response.setMessageError("Track is removed");
            return response.getResponseAsResponseEntity();
        } else {

            for (Point point : points) {
                point.setTrackId(id);
                pointRepository.save(point);
            }

            Track updated = tracksService.updateTrackSummary(track,points);
            trackRepository.save(updated);

            return new ResponseEntity(null, HttpStatus.CREATED);
        }

    }

    @GetMapping(path = "/{id}/navigate")
    public @ResponseBody
    ResponseEntity<?> getNavigationPoint(@PathVariable int id, Authentication auth) {
        JsonResponse response = new JsonResponse();

        String username = auth.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        Track track = trackRepository.findById(id);
        List<Point> points = pointRepository.findAllByTrackIdOrderByTimestampAsc(id);

        if (track == null || points == null || user.getId() != track.getUserId()) {
            response.setMessageError("An error occurred");
            return response.getResponseAsResponseEntity();
        } else {
            return new ResponseEntity(points.get(points.size() - 1), HttpStatus.OK);
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(TrackController.class, args);
    }
}
