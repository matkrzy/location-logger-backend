package com.locationtracker.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int userId;

    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true, updatable = false)
    private String uuid;

    private boolean removed;

    public Device() {

    }

    public Device(String name, int user_id) {
        this.name = name;
        this.userId = user_id;
        this.uuid = UUID.randomUUID().toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
