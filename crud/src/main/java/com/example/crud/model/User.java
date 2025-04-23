package com.example.crud.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String id;
    private String name;
    private Set<String> followers;   // Lista de IDs dos usuários que seguem este usuário
    private Set<String> following;   // Lista de IDs dos usuários que este usuário segue
    private Set<PostDTO> posts;      // Lista de postagens do usuário

    // Construtor
    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.posts = new HashSet<>();
    }

    // Métodos para interações com o usuário
    public void follow(User user) {
        this.following.add(user.getId());
        user.addFollower(this);  // Adiciona o usuário à lista de seguidores do "seguido"
    }

    public void addFollower(User user) {
        this.followers.add(user.getId());
    }

    public void postMessage(PostDTO post) {
        posts.add(post);  // Adiciona uma nova postagem
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public Set<String> getFollowing() {
        return following;
    }

    public Set<PostDTO> getPosts() {
        return posts;
    }
}
