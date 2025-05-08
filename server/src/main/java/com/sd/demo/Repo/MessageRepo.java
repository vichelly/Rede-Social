package com.sd.demo.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.sd.demo.Entities.Message;
import com.sd.demo.Entities.User;

@Component
public class MessageRepo {
    private Map<Long, Message> messages = new ConcurrentHashMap<>();
    private long messageIdCounter = 1; // Para gerar IDs únicos para as mensagens

    // Salvar a mensagem
    public Message save(Message message) {
        // Definindo o ID da mensagem (incrementando o contador)
        message.setId(messageIdCounter++);
        messages.put(message.getId(), message);
        return message;
    }

    // Buscar uma mensagem por ID
    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(messages.get(id));
    }

    // Buscar mensagens por receptor
    public List<Message> findByReceiver(User receiver) {
        List<Message> userMessages = new ArrayList<>();
        for (Message message : messages.values()) {
            if (message.getReceiver().equals(receiver)) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    // Retornar todas as mensagens
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }
}
