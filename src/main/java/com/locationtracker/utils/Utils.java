package com.locationtracker.utils;

import com.locationtracker.model.Point;

import java.util.Date;

public class Utils {
    private Double rad(Double x) {
        return x * Math.PI / 180;
    }

    public Double getDistance(Point start, Point end) {
        int R = 6378137;
        Double d = 0.0;
        try {
            Double p1Lat = start.getLatitude();
            Double p1Lng = start.getLongitude();

            Double p2Lat = end.getLatitude();
            Double p2Lng = end.getLongitude();

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

    public Date getTimeDiffInMinutes(Point start, Point end) {

        long diff = end.getTimestamp().getTime() - start.getTimestamp().getTime();
        long timeZoneOffset = 3600000;
        Date d = new Date();
        d.setTime(diff - timeZoneOffset);

        return d;
    }
}
