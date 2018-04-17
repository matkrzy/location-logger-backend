package com.locationtracker.utils;

import com.locationtracker.model.Point;
import com.locationtracker.model.User;
import com.locationtracker.repository.UserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

@Service
public class Utils {
    @Autowired
    public UserRepository userRepository;

    private Double rad(Double x) {
        return x * Math.PI / 180;
    }

    public Double getDistance(Point start, Point end) {
        int R = 6378137;
        Double d = 0.0;
        try {
            Double p1Lat = start.getLat();
            Double p1Lng = start.getLng();

            Double p2Lat = end.getLat();
            Double p2Lng = end.getLng();

            Double dLat = rad(p2Lat - p1Lat);
            Double dLong = rad(p2Lng - p1Lng);
            Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(rad(p1Lat)) *
                            Math.cos(rad(p2Lat)) *
                            Math.sin(dLong / 2) *
                            Math.sin(dLong / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            d = R * c;


        } catch (Exception e) {
            System.out.print(e.toString());
        }

        return d;
    }

    public LocalTime getTimeDiff(Point start, Point end) {

        LocalTime endTimesamp = LocalTime.of(end.getTimestamp().getHours(), end.getTimestamp().getMinutes(), end.getTimestamp().getSeconds());
        LocalTime startTimestam = LocalTime.of(start.getTimestamp().getHours(), start.getTimestamp().getMinutes(), start.getTimestamp().getSeconds());

        endTimesamp = endTimesamp.minusHours(startTimestam.getHour());
        endTimesamp = endTimesamp.minusMinutes(startTimestam.getMinute());
        endTimesamp = endTimesamp.minusSeconds(startTimestam.getSecond());
        return endTimesamp;
    }

    public Date getTimeDiffInMinutes(Point start, Point end, Point add) {

        long diff = end.getTimestamp().getTime() - start.getTimestamp().getTime();
        diff += start.getTimestamp().getTime() - add.getTimestamp().getTime();
        long timeZoneOffset = 3600000;
        Date d = new Date();
        d.setTime(diff - timeZoneOffset);

        return d;
    }
}
