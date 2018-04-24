package com.locationtracker.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track_id")
    private int trackId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", columnDefinition="DATETIME")
    private Date timestamp;

    private Double latitude;

    private Double longitude;

    private Double altitude = 0.0;

    private Double speed;

    private Double distance;

    @Temporal(TemporalType.TIME)
    @Column(name = "duration", columnDefinition="TIME")
    private Date duration;

    public Point(){}

    public Long getId() {
        return id;
    }

    public int getTrackId() {
        return trackId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getDistance() {
        return distance;
    }

    public Date getDuration() {
        return duration;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }
}
