package com.sd.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.protobuf.Message;
import com.sd.demo.Entities.User;

public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByReceiver(User receiver);
}

