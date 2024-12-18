package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.APIResponse;
import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.DTOs.MessageDTO_2;
import com.chatapp.dev1.Entities.DTOs.MessageDTO_Receive;
import com.chatapp.dev1.Entities.Message;
import com.chatapp.dev1.Entities.DTOs.MessageDTO;
import com.chatapp.dev1.Entities.User;
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

            Message savedMessage = messageService.saveMessage(message);

            return new ResponseEntity<>(new MessageDTO_2(savedMessage.getSender().getUsername(), savedMessage.getMessageText(), savedMessage.getSentAt()), HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/write2")
    public ResponseEntity<?> writeMessage2(@RequestBody MessageDTO_Receive messageDTO_receive){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

        Chat chat = chatService.getChatById(Long.parseLong(messageDTO_receive.getChatId()));
        User authenticatedUser = userService.getUserByUsername(usernameFromSecurity);

//       check if user has rights to this chat
        if ( !chatService.existsByChatChatId ( Long.parseLong(messageDTO_receive.getChatId()) )
                || !participantService.participantExistsByChatRefAndUserRef(chat, authenticatedUser) ){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        Message message = new Message();
        message.setMessageText(messageDTO_receive.getText());
        message.setChat(chat);
        message.setSender(authenticatedUser);

        Message savedMessage = messageService.saveMessage(message);

        log.info("savedmessage text: " + savedMessage.getMessageText() + " sender: " + savedMessage.getSender().getUsername());

        return new ResponseEntity<>(new MessageDTO_2(savedMessage.getSender().getUsername(), savedMessage.getMessageText(), savedMessage.getSentAt()), HttpStatus.CREATED);
    }
    
    @GetMapping("/get-messages/{chat_id}")
    public ResponseEntity<?> getAllMessagesFromChatId(@PathVariable Long chat_id){
        if(!chatService.existsByChatChatId(chat_id)){
            return new ResponseEntity<>(new APIResponse(false, "no such chat exists", null), HttpStatus.BAD_REQUEST);
        }

        Chat chat;
        if (chatService.existsByChatChatId(chat_id)){
            chat = chatService.getChatById(chat_id);
        }
        else{
            return new ResponseEntity<>(new APIResponse(false, "no such chat exists", null), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

        User authenticatedUser = userService.getUserByUsername(usernameFromSecurity);

//       check if user has rights to this chat
        if (!chatService.existsByChatChatId(chat_id)
                || !participantService.participantExistsByChatRefAndUserRef(chat, authenticatedUser)){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Message> messages = messageService.getAllMessagesByChatId(chat_id);

        List<MessageDTO_2> messageDTO_2List = new java.util.ArrayList<>(List.of());

        for(int i = 0; i < messages.size(); i++){
            messageDTO_2List.add(new MessageDTO_2(messages.get(i).getSender().getUsername(), messages.get(i).getMessageText(), messages.get(i).getSentAt()));
        }

        return new ResponseEntity<>(messageDTO_2List, HttpStatus.OK);

//        if (messages != null) {
//            return new ResponseEntity<>(messages, HttpStatus.OK);
//        }
//        log.info("not found any messages");
//        return ResponseEntity.noContent().build();
    }
    
    private boolean isValidMessage(Message message) {
        return message.getChat() != null && message.getSender() != null;
    }

    private boolean isValidMessageDTO(MessageDTO messageDTO) {
        return messageDTO.chatId != null && messageDTO.senderUserId != null && messageDTO.message != null;
    }
}
