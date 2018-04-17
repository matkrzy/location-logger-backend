package com.locationtracker.Model;

import javax.persistence.*;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track_id")
    private int trackId;

    private String timestamp;

    private float latitude;

    private float longitude;

    private float altitude;

    private float speed;

    private int direction;

    public Point(){}

    public Long getId() {
        return id;
    }

    public int getTrackId() {
        return trackId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
