package com.sd.demo.Entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @ManyToMany
    private Set<User> following = new HashSet<>();

    public Set<User> getFollowing() {
        return following;
    }
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
