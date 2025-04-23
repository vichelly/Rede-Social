package com.example.crud.model;

public class FollowDTO {

    private String followerId;
    private String followedId;

    public FollowDTO() {}

    public FollowDTO(String followerId, String followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowedId() {
        return followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }
}