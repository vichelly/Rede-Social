package com.sd.demo.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    public String getContent() {
        return content;
    }
    public Long getId() {
        return id;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public User getUser() {
        return user;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
