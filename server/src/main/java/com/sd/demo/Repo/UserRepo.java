package com.sd.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Optional;
import com.sd.demo.Entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
}
