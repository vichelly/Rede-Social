package com.sd.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.User;
import com.sd.demo.Repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User createUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        User user = new User(username);
        return userRepo.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public void followUser(User follower, User followee) {
        if (follower == null || followee == null) {
            throw new IllegalArgumentException("Users cannot be null.");
        }
        follower.follow(followee.getUsername());
        userRepo.save(follower); // Re-salva o seguidor atualizado
    }
}
