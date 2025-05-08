package com.sd.demo.Service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.User;
import com.sd.demo.Repo.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo UserRepo;

    public User getUserByUsername(String username){
        return UserRepo.findByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User creatUser(String username){
        User user = new User();
        user.setUsername(username);
        return UserRepo.save(user);
    }

    public void followUser(User follower, User followee){
        follower.getFollowing().add(followee);
        UserRepo.save(follower);
    }
}
