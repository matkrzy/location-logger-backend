package com.locationtracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @NotNull
    @Column(name = "device_id")
    private int deviceId;

    private Boolean removed = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", columnDefinition = "DATETIME")
    private Date date;

    @Min(0)
    @NumberFormat(pattern="#.##")
    private Double distance;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(pattern="H:mm")
    @Column(name = "duration", columnDefinition = "TIME")
    private LocalTime duration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", columnDefinition = "DATETIME")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", columnDefinition = "DATETIME")
    private Date endTime;

    @Min(0)
    @NumberFormat(pattern="#.##")
    @Column(name = "min_speed")
    private Double minSpeed;

    @Min(0)
    @NumberFormat(pattern="#.##")
    @Column(name = "max_speed")
    private Double maxSpeed;

    @Min(0)
    @NumberFormat(pattern="#.##")
    @Column(name = "avg_speed")
    private Double avgSpeed;

    @Min(0)
    @NumberFormat(pattern="#.##")
    @Column(name = "min_altitude")
    private Double minAltitude;

    @Min(0)
    @NumberFormat(pattern="#.##")
    @Column(name = "max_altitude")
    private Double maxAltitude;

    @Min(0)
    @NumberFormat(pattern="#.##")
    @Column(name = "avg_altitude")
    private Double avgAltitude;

    public Track(){}

    public Track(String name, int userId, int deviceId) {
        this.name = name;
        this.userId = userId;
        this.deviceId = deviceId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public Boolean isRemoved() {
        return removed;
    }

    public Date getDate() {
        return date;
    }

    public Double getDistance() {
        return distance;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public Double getMinAltitude() {
        return minAltitude;
    }

    public Double getMaxAltitude() {
        return maxAltitude;
    }

    public Double getAvgAltitude() {
        return avgAltitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public void setMinAltitude(Double minAltitude) {
        this.minAltitude = minAltitude;
    }

    public void setMaxAltitude(Double maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public void setAvgAltitude(Double avgAltitude) {
        this.avgAltitude = avgAltitude;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
