package com.sd.demo.Entities;

import java.time.LocalDateTime;

public class Post {

    private long id;
    private User user;
    private String content;
    private LocalDateTime timestamp;

    public Post() {}

    public Post(User user, String content, LocalDateTime timestamp) {
        this.user = user;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
