package com.sd.demo.Controller;

import java.util.List;

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

@RestController
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // Criar um novo post
    @PostMapping("/create")
    public Post createPost(@RequestBody Post post) {
        // Verificar se o usuário existe
        User user = userService.getUserByUsername(post.getUser().getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        // Criar o post
        return postService.createPost(user, post.getContent());
    }

    // Buscar posts de um usuário
    @GetMapping("/user/{username}")
    public List<Post> getUserPosts(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return postService.getUserPosts(user);
    }
}
