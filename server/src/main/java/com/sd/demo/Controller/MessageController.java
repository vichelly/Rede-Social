package com.sd.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/send")
    public Message sendMessage(@RequestParam String senderUsername, @RequestParam String receiverUsername, @RequestParam String content) {
        User sender = userService.getUserByUsername(senderUsername);
        User receiver = userService.getUserByUsername(receiverUsername);
        return messageService.sendMessage(sender, receiver, content);
    }

    @GetMapping("/user/{username}")
    public List<Message> getMessages(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return messageService.getMessagesForUser(user);
    }
}

