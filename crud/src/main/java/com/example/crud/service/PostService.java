package com.example.crud.service;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.crud.model.PostDTO;

@Service
public class PostService {

    private final LoggerService logger = new LoggerService("server");

    public void publish(PostDTO post) {
        int timestamp = LogicalClock.tick();
        post.setLogicalTimestamp(timestamp);

        // armazena a postagem
        MemoryStorage.posts
            .computeIfAbsent(post.getUserId(), k -> new ArrayList<>())
            .add(post);

        // log
        logger.log("[POST] " + post.getUserId() + " postou: '" + post.getContent() + "' @t=" + timestamp);

        // notifica seguidores (simulado)
        Set<String> followers = MemoryStorage.followers.getOrDefault(post.getUserId(), Set.of());
        for (String follower : followers) {
            logger.log("[NOTIF] " + follower + " notificado de novo post de " + post.getUserId());
        }
    }
}
