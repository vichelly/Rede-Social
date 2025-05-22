package com.sd.demo.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.User;
import com.sd.demo.Repo.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    public User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        return userRepo.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void followUser(User follower, User followee) {
        follower.getFollowing().add(followee.getUsername());
        userRepo.save(follower);
    }

    public List<String> getFollowers(String username) {
        List<String> followers = new ArrayList<>();
        for (User user : userRepo.findAll()) {
            if (user.getFollowing().contains(username)) {
                followers.add(user.getUsername());
            }
        }
        return followers;
    }
}
