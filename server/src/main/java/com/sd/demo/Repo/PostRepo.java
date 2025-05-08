package com.sd.demo.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.sd.demo.Entities.Post;
import com.sd.demo.Entities.User;

@Component
public class PostRepo {
    private Map<Long, Post> posts = new ConcurrentHashMap<>();
    private long postIdCounter = 1; // Para gerar IDs únicos para os posts

    // Salvar o post
    public Post save(Post post) {
        // Definindo o ID do post (incrementando o contador)
        post.setId(postIdCounter++);
        posts.put(post.getId(), post);
        return post;
    }

    // Buscar um post por ID
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    // Buscar posts por usuário
    public List<Post> findByUser(User user) {
        List<Post> userPosts = new ArrayList<>();
        for (Post post : posts.values()) {
            if (post.getUser().equals(user)) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }

    // Retornar todos os posts
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
}
