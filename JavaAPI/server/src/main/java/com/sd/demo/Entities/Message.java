package com.sd.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private int timestamp;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public String getContent() {
        return content;
    }
    public Long getId() {
        return id;
    }
    public User getReceiver() {
        return receiver;
    }
    public User getSender() {
        return sender;
    }
    public int getTimestamp() {
        return timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

