package com.example.crud.service;

import com.example.crud.model.FollowDTO;
import com.example.crud.model.PostDTO;
import com.example.crud.model.User;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private final Map<String, User> users;

    public CreateUserService() {
        this.users = new HashMap<>();
        createUsers();
    }

    private void createUsers() {
        User user1 = new User("user1", "Alice");
        User user2 = new User("user2", "Bob");
        User user3 = new User("user3", "Charlie");

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);

        user1.follow(user2);
        user2.follow(user3);

        PostDTO post1 = new PostDTO(user1.getId(), "Hello World from Alice!");
        PostDTO post2 = new PostDTO(user2.getId(), "Hello World from Bob!");

        user1.postMessage(post1);
        user2.postMessage(post2);
    }

    public void followUser(FollowDTO followDTO) {
        User follower = users.get(followDTO.getFollowerId());
        User followed = users.get(followDTO.getFollowedId());

        if (follower != null && followed != null) {
            follower.follow(followed);
        }
    }

    public void postMessage(String userId, PostDTO post) {
        User user = users.get(userId);
        if (user != null) {
            user.postMessage(post);
        }
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }
}