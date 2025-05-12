package com.sd.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sd.demo.DTO.MessageDTO;
import com.sd.demo.Entities.Message;
import com.sd.demo.Error.ResponseError;
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

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        // Busca os usuários pelo username
        User sender = userService.getUserByUsername(messageDTO.getSenderUsername());
        User receiver = userService.getUserByUsername(messageDTO.getReceiverUsername());
        
        if (sender == null || receiver == null) {
            // Retorna uma resposta de erro com mensagem personalizada
            ResponseError error = new ResponseError("Sender or receiver not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Message message = messageService.sendMessage(sender, receiver, messageDTO.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Message> messages = messageService.getMessagesForUser(user);
        return ResponseEntity.ok(messages);
    }

    // Novo método para buscar mensagens recebidas por um usuário
    @GetMapping("/received/{username}")
    public ResponseEntity<List<Message>> getReceivedMessages(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Message> messages = messageService.getMessagesForReceiver(user);
        return ResponseEntity.ok(messages);
    }
}
