package com.example.crud.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.crud.model.MessageDTO;

@Service
public class MessageService {

    private final LoggerService logger = new LoggerService("server");

    public void sendMessage(MessageDTO msg) {
        int timestamp = LogicalClock.tick();
        msg.setLogicalTimestamp(timestamp);

        MemoryStorage.privateMessages
            .computeIfAbsent(msg.getToUserId(), k -> new ArrayList<>())
            .add(msg);

        logger.log("[MSG] " + msg.getFromUserId() + " -> " + msg.getToUserId() + ": '" + msg.getContent() + "' @t=" + timestamp);
    }
}