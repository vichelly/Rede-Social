package com.example.crud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.model.PostDTO;
import com.example.crud.service.PostService;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController( PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<String> publishPost(@RequestBody PostDTO post){
        postService.publish(post);
        return ResponseEntity.ok("Post publicado");
    }
}
