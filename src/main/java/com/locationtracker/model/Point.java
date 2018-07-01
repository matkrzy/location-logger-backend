package com.locationtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    @Min(0)
    @Column(name = "track_id")
    private int trackId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", columnDefinition = "DATETIME")
    private Date timestamp;

    @Column(name = "latitude")
    private Double lat;

    @Column(name = "longitude")
    private Double lng;

    private Double altitude = 0.0;

    @Min(0)
    private Double speed;

    @Min(0)
    private Double distance;

    @Temporal(TemporalType.TIME)
    @Column(name = "duration", columnDefinition = "TIME")
    private Date duration;

    public Point() {
    }

    public Long getId() {
        return id;
    }

    public int getTrackId() {
        return trackId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
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
