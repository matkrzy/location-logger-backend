package com.locationtracker.model;

import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email
    @NotBlank
    @NotNull
    private String username;

    @NotNull
    @NotBlank
    @Column(name = "password_hash")
    private String password;

    private boolean removed;

    public User(){};

    public User(User user){
        username = user.username;
        password = user.password;
        removed = false;
    };

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
