package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChatController.class);

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats(){
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<Chat>> getAllChatsByUserId(@PathVariable Long user_id){
        return new ResponseEntity<>(chatService.getAllChatsByUserId(user_id), HttpStatus.OK);
    }

    @GetMapping("/id/{chatId}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long chatId) {
        return new ResponseEntity<Chat>(chatService.getChatById(chatId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        log.info("create chat -> " + chat);
        return new ResponseEntity<Chat>(chatService.saveChat(chat), HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{chatId}")
    public void deleteChatById(@PathVariable Long chatId) {
        chatService.deleteChatById(chatId);
    }
}
