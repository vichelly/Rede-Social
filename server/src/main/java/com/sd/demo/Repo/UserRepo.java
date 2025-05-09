package com.sd.demo.Repo;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import java.util.Optional; 

import com.sd.demo.Entities.User;

@Component
public class UserRepo {
    private Map<String, User> users = new ConcurrentHashMap<>();

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public User save(User user) {
        users.put(user.getUsername(), user);
        return user;
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
