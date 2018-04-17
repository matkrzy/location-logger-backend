package com.locationtracker.Model;

import javax.persistence.*;

@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "device_id")
    private int deviceId;

    private Boolean removed = false;

    public Track() {
    }

    public Track(String name, int userId, int deviceId){
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

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }
}
