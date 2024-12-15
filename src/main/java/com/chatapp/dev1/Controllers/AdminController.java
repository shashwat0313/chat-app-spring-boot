package com.chatapp.dev1.Controllers;


import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.Message;
import com.chatapp.dev1.Entities.Participant;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Services.ChatService;
import com.chatapp.dev1.Services.MessageService;
import com.chatapp.dev1.Services.ParticipantService;
import com.chatapp.dev1.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ParticipantService participantService;
    private final ChatService chatService;
    private final MessageService messageService;

    public AdminController(UserService userService, ParticipantService participantService, ChatService chatService, MessageService messageService) {
        this.userService = userService;
        this.participantService = participantService;
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @GetMapping("/all-users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/all-participants")
    public ResponseEntity<List<Participant>> getAllParticipants(){
        return new ResponseEntity<>(participantService.getAllParticipants(), HttpStatus.OK);
    }

    @GetMapping("/all-chats")
    public ResponseEntity<List<Chat>> getAllChats(){
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }

    @GetMapping("/all-messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }
}
