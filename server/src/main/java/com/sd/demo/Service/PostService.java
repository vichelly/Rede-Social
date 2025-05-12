package com.sd.demo.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.Post;
import com.sd.demo.Entities.User;
import com.sd.demo.Repo.PostRepo;

@Service
public class PostService {
    
    @Autowired
    private PostRepo postRepository;

    // Criar um novo post
    public Post createPost(User user, String content) {
        Post post = new Post(user, content, LocalDateTime.now());
        return postRepository.save(post);
    }

    // Buscar posts de um usuário
    public List<Post> getUserPosts(User user) {
        return postRepository.findByUser(user);
    }
}
