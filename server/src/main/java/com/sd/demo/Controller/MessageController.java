package com.sd.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sd.demo.DTO.MessageDTO;
import com.sd.demo.Entities.Message;
import com.sd.demo.Entities.User;
import com.sd.demo.Service.MessageService;
import com.sd.demo.Service.UserService;

@RestController
@RequestMapping("/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;

    // Alteração: Usando @RequestBody para capturar os parâmetros do JSON
    @PostMapping("/send")
    public Message sendMessage(@RequestBody MessageDTO messageDTO) {
        // Busca os usuários pelo username
        User sender = userService.getUserByUsername(messageDTO.getSenderUsername());
        User receiver = userService.getUserByUsername(messageDTO.getReceiverUsername());
        
        if (sender == null || receiver == null) {
            throw new RuntimeException("Sender or receiver not found");
        }

        return messageService.sendMessage(sender, receiver, messageDTO.getContent());
    }

    @GetMapping("/user/{username}")
    public List<Message> getMessages(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return messageService.getMessagesForUser(user);
    }
}
