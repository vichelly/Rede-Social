package com.sd.demo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sd.demo.Entities.User;
import com.sd.demo.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Criação de novo usuário
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        System.out.println("Recebido username: " + user.getUsername());
        return userService.createUser(user.getUsername());
    }

    // Obter usuário por username
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // Seguir outro usuário
    @PostMapping("/{username}/follow")
    public void followUser(@PathVariable String username, @RequestParam String followeeUsername) {
        User follower = userService.getUserByUsername(username);
        User followee = userService.getUserByUsername(followeeUsername);
        if (follower != null && followee != null) {
            userService.followUser(follower, followee);
        } else {
            throw new RuntimeException("Usuário(s) não encontrado(s)");
        }
    }

    // Listar quem o usuário está seguindo
    @GetMapping("/{username}/following")
    public List<String> getFollowing(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return new ArrayList<>(user.getFollowing()); // converte Set para List
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    // Listar seguidores do usuário
    @GetMapping("/{username}/followers")
    public List<String> getFollowers(@PathVariable String username) {
        return userService.getFollowers(username);
    }
}
