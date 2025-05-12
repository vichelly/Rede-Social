package com.sd.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sd.demo.Entities.User;
import com.sd.demo.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        System.out.println("Recebido username: " + user.getUsername());
        return userService.createUser(user.getUsername());
    }



    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/{username}/follow")
    public void followUser(@PathVariable String username, @RequestParam String followeeUsername) {
        User follower = userService.getUserByUsername(username);
        User followee = userService.getUserByUsername(followeeUsername);
        userService.followUser(follower, followee);
    }
}
