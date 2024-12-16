package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.APIResponse;
import com.chatapp.dev1.Entities.Message;
import com.chatapp.dev1.Entities.DTOs.MessageDTO;
import com.chatapp.dev1.Services.ChatService;
import com.chatapp.dev1.Services.MessageService;
import com.chatapp.dev1.Services.ParticipantService;
import com.chatapp.dev1.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;
    private final ParticipantService participantService;

    public MessageController(MessageService messageService, UserService userService, ChatService chatService, ParticipantService participantService) {
        this.messageService = messageService;
        this.userService = userService;
        this.chatService = chatService;
        this.participantService = participantService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages(){
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeMessage(@RequestBody MessageDTO messageDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

        if(!usernameFromSecurity.equals(messageDTO.senderUserId.toString())){
            return new ResponseEntity<>(new APIResponse(false, "Unauthorised to send with given name", null), HttpStatus.BAD_REQUEST);
        }

        if(isValidMessageDTO(messageDTO)){
            Message message = new  Message();
            message.setMessageText(messageDTO.getMessage());
            message.setSender( userService.getUserByUserId(messageDTO.senderUserId) );
            message.setChat( chatService.getChatById(messageDTO.chatId) );

            if( !isValidMessage(message)
                    || !userService.existsByUserId(message.getSender().getUserId())
                    || !chatService.existsByChatChatId(message.getChat().getChatId())
                    || !participantService.participantExistsByChatRefAndUserRef(message.getChat(), message.getSender())
            ){
                return new ResponseEntity<>(new APIResponse(false, "sender not member of chat or message invalid", null), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(messageService.saveMessage(message), HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/get-messages/{chat_id}")
    public ResponseEntity<?> getAllMessagesFromChatId(@PathVariable Long chat_id){
        if(!chatService.existsByChatChatId(chat_id)){
            return new ResponseEntity<>(new APIResponse(false, "no such chat exists", null), HttpStatus.BAD_REQUEST);
        }

        List<Message> messages = messageService.getAllMessagesByChatId(chat_id);

        if (messages != null) {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
        log.info("not found any messages");
        return ResponseEntity.noContent().build();
    }
    
    private boolean isValidMessage(Message message) {
        return message.getChat() != null && message.getSender() != null;
    }

    private boolean isValidMessageDTO(MessageDTO messageDTO) {
        return messageDTO.chatId != null && messageDTO.senderUserId != null && messageDTO.message != null;
    }
}
