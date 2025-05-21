package com.sd.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sd.demo.Entities.User;
import com.sd.demo.Repo.MessageRepo;
import com.sd.demo.Clock.LogicalClock;
import com.sd.demo.Entities.Message;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private LogicalClock logicalClock;

    public Message sendMessage(User sender, User receiver, String content) {
        int timestamp = logicalClock.tick(); // incrementa relógio
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(timestamp);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesForUser(User user) {
        return messageRepository.findByReceiver(user);
    }

    // Novo método para buscar mensagens recebidas
    public List<Message> getMessagesForReceiver(User receiver) {
        return messageRepository.findByReceiver(receiver);
    }
}

