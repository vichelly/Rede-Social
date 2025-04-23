package com.example.crud.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.model.FollowDTO;
import com.example.crud.model.PostDTO;
import com.example.crud.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para seguir um usuário
    @PostMapping("/follow")
    public String follow(@RequestBody FollowDTO followDTO) {
        userService.followUser(followDTO);
        return "User " + followDTO.getFollowerId() + " is now following " + followDTO.getFollowedId();
    }

    // Endpoint para criar uma postagem
    @PostMapping("/{userId}/post")
    public String postMessage(@PathVariable String userId, @RequestBody PostDTO post) {
        userService.postMessage(userId, post);
        return "Post created by " + userId;
    }
}