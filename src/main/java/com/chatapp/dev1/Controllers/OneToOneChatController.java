package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.DTOs.OneToOneChatDTO;
import com.chatapp.dev1.Entities.Participant;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Services.ChatService;
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
@RequestMapping()
public class OneToOneChatController {

    private final ChatService chatService;
    private final ParticipantService participantService;
    private final UserService userService;

    public OneToOneChatController(ChatService chatService, ParticipantService participantService, UserService userService) {
        this.chatService = chatService;
        this.participantService = participantService;
        this.userService = userService;
    }

    @PostMapping("/new-chat")
    public ResponseEntity<?> createOneToOneChat(@RequestBody OneToOneChatDTO oneToOneChatDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

//        chat from the user to the same user MUST not be created
        String friendUsername = oneToOneChatDTO.getFriendUsername();

        if(friendUsername.equals(usernameFromSecurity)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

//        we now need to create a new chat
//        1. create a chat
//        2. create a participant for both the users for the new chat
//        3. send success and chat object to the user
        try{

//        1 create chat
            Chat chat = new Chat();
            chat.setChatName(oneToOneChatDTO.getChatName());
            chat.setIsGroup(false);

            Chat savedChat = chatService.saveChat(chat);

            log.info("got the chat with id: " + savedChat.getChatId());

    //        2. create par on the same chat

    //        2.1 get self user
            User userSelf = userService.getUserByUsername(usernameFromSecurity);

            log.info("userself's userid: " + userSelf.getUserId());

    //        2.2 init self participant
            Participant participantSelf = new Participant();
    //        2.3 set current chat
            participantSelf.setChat(savedChat);
    //        2.4 set self user to participant object
            participantSelf.setUser(userSelf);

    //       2.5 get friend user
            User userFriend = userService.getUserByUsername(oneToOneChatDTO.getFriendUsername());

            log.info("userfriend's userid: " + userFriend.getUserId());

            //       2.6 init friend participant
            Participant participantFriend = new Participant();
    //       2.7 set current chat
            participantFriend.setChat(savedChat);
    //       2.8 set friend user to friend participant
            participantFriend.setUser(userFriend);

    //        2.9 save both the participants
            participantService.saveParticipant(participantSelf);
            participantService.saveParticipant(participantFriend);

            OneToOneChatDTO oneToOneChatDTOResponse = new OneToOneChatDTO(savedChat.getChatId().toString(), savedChat.getChatName(), userFriend.getUsername());

            return new ResponseEntity<>(oneToOneChatDTOResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("exception in createoneToOneChat: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @PostMapping
    public ResponseEntity<?> exitOneToOneChat(@RequestBody Long chatId){
//        find if the user really is a chatter in the given chat
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

        User authenticatedUser = userService.getUserByUsername(usernameFromSecurity);

        List<Chat> userChatList = chatService.getAllChatsByUserId(authenticatedUser.getUserId());

        Chat chat = chatService.getChatById(chatId);

        Boolean isPresent = userChatList.stream()
                .anyMatch(chatInList -> chatInList.getChatId().equals(chatId));

        if(!isPresent){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

//        remove the participant now
        participantService.deleteParticipantByUserId(authenticatedUser.getUserId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
