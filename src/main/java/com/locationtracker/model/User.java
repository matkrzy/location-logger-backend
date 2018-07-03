package com.locationtracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.locationtracker.enums.UserRole;

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
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    @NotBlank
    @Column(name = "password_hash")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition="enum('USER','ADMIN')")
    private UserRole role = UserRole.USER;

    public User(){};

    public User(User user){
        username = user.username;
        password = user.password;
        role = UserRole.USER;
        active = false;
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

    public boolean isActive() {
        return active;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }
}
