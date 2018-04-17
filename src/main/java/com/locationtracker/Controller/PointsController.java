package com.locationtracker.Controller;

import com.locationtracker.Model.Point;
import com.locationtracker.Repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/points")
public class PointsController {
    @Autowired
    public PointRepository pointRepository;

    @GetMapping(path = "/{id}")
    public @ResponseBody
    List<Point> getPointsByTrackId(@PathVariable Long id){

        return pointRepository.findAllById(id);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(PointsController.class, args);
    }
}
