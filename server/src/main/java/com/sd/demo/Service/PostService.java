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

    public Post createPost(User user, String content) {
        Post post = new Post();
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());
        post.setUser(user);
        return postRepository.save(post);
    }

    public List<Post> getUserPosts(User user) {
        return postRepository.findByUser(user);
    }
}

