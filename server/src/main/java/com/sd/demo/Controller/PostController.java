package com.sd.demo.Controller;

import java.util.List;

import com.sd.demo.GrpcService.PostStreamManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sd.demo.Entities.Post;
import com.sd.demo.Entities.User;
import com.sd.demo.Service.PostService;
import com.sd.demo.Service.UserService;

import post.PostResponse;

@RestController
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostStreamManager streamManager;

    @PostMapping("/create")
    public Post createPost(@RequestBody Post post) {
        User user = userService.getUserByUsername(post.getUser().getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Criação do post
        Post newPost = postService.createPost(user, post.getContent());

        // Construir PostResponse gRPC para envio
        PostResponse response = PostResponse.newBuilder()
            .setUsername(user.getUsername())
            .setContent(newPost.getContent())
            .setTimestamp(Long.toString(System.currentTimeMillis()))
            .build();

        // Notificar clientes gRPC
        streamManager.notifySubscribers(response);

        return newPost;
    }

    @GetMapping("/user/{username}")
    public List<Post> getUserPosts(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return postService.getUserPosts(user);
    }
}