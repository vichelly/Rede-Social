package com.sd.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.Post;
import com.sd.demo.Entities.User;
import com.sd.demo.Repo.PostRepo;
import com.sd.demo.Clock.LogicalClock;

@Service
public class PostService {
    
    @Autowired
    private PostRepo postRepository;

    @Autowired
    private LogicalClock logicalClock;

    // Criar um novo post
    public Post createPost(User user, String content) {
        int timestamp = logicalClock.tick(); // incrementa relógio
        Post post = new Post(user, content, timestamp);
        postRepository.save(post);
        log("Post criado por " + user.getUsername() + " em " + timestamp);
        replicateToPeers(post); // função que ainda vamos criar
        return postRepository.save(post);
    }

    // Buscar posts de um usuário
    public List<Post> getUserPosts(User user) {
        return postRepository.findByUser(user);
    }
    // Método para registrar logs (pode ser simples System.out por enquanto)
    private void log(String message) {
        System.out.println("[PostService] " + message);
    }
    
    // Método para replicar post para outros servidores (deixa vazio por enquanto)
    private void replicateToPeers(Post post) {
        // TODO: implementar replicação entre servidores
    }
}
