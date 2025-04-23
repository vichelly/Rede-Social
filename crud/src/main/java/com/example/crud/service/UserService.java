package com.example.crud.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.example.crud.model.FollowDTO;
import com.example.crud.model.PostDTO;

@Service
public class UserService {

    private final LoggerService logger = new LoggerService("server");

    public void followUser(FollowDTO dto) {
        MemoryStorage.followers
            .computeIfAbsent(dto.getFollowedId(), k -> new HashSet<>())
            .add(dto.getFollowerId());

        logger.log("[FOLLOW] " + dto.getFollowerId() + " seguiu " + dto.getFollowedId());
    }

    public void postMessage(String userId, PostDTO post) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'postMessage'");
    }

    public Object getUser(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

}
