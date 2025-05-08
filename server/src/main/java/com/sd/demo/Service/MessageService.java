package com.sd.demo.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.User;
import com.sd.demo.Repo.MessageRepo;
import com.sd.demo.Entities.Message;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepository;

    public Message sendMessage(User sender, User receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessagesForUser(User user) {
        return messageRepository.findByReceiver(user);
    }
}

