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

    // Armazena as mensagens com seus respectivos IDs
    private final Map<Long, Message> messages = new ConcurrentHashMap<>();

    // Contador para gerar IDs únicos
    private long messageIdCounter = 1;

    /**
     * Salva uma nova mensagem no repositório em memória.
     */
    public synchronized Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("Mensagem não pode ser nula");

        message.setId(messageIdCounter++);
        messages.put(message.getId(), message);
        return message;
    }

    /**
     * Busca uma mensagem pelo seu ID.
     */
    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(messages.get(id));
    }

    /**
     * Retorna todas as mensagens recebidas por um usuário.
     */
    public List<Message> findByReceiver(User receiver) {
        List<Message> userMessages = new ArrayList<>();

        if (receiver == null) return userMessages;

        for (Message message : messages.values()) {
            if (receiver.equals(message.getReceiver())) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    /**
     * Retorna todas as mensagens salvas.
     */
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }
}
