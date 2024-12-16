package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.DTOs.OneToOneChatDTO;
import com.chatapp.dev1.Entities.Participant;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Services.ChatService;
import com.chatapp.dev1.Services.ParticipantService;
import com.chatapp.dev1.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;


@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final UserService userService;
    private final ParticipantService participantService;

    public ChatController(ChatService chatService, UserService userService, ParticipantService participantService) {
        this.chatService = chatService;
        this.userService = userService;
        this.participantService = participantService;
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats(){
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getAllChatsByUserId(@PathVariable Long user_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

        User authenticatedUser = userService.getUserByUsername(usernameFromSecurity);

        log.info("got user in getallchats with user id: " + authenticatedUser.getUserId());

//        protection
        if(!authenticatedUser.getUserId().equals(user_id)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Chat> userChatList = chatService.getAllChatsByUserId(authenticatedUser.getUserId());

        log.info("chat count for the user is: " + userChatList.size());

//        we need to return an object like {chatName. FriendName}
        List<OneToOneChatDTO> chatListForResponse = new java.util.ArrayList<>(List.of());

        for(int i = 0; i < userChatList.size(); i++){
            List<Participant> participantsInChat = participantService.getParticipantsByChatId(userChatList.get(i).getChatId());
            Stream<Participant> participantStream = participantsInChat.stream().filter(participant -> !participant.getUser().getUserId().equals(authenticatedUser.getUserId()));
            String friendName = participantStream.toList().getFirst().getUser().getUsername();

            chatListForResponse.add(new OneToOneChatDTO(userChatList.get(i).getChatName(), friendName));
        }

        return new ResponseEntity<>(chatListForResponse, HttpStatus.OK);
    }

    @GetMapping("/id/{chatId}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long chatId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

//        add checks to ensure the user is associated with the chat
//        get all chats under the user
//        check if the chat is in the list, if no then unauthorised

//        1. get the chat
        Chat chat = chatService.getChatById(chatId);

        User authenticatedUser = userService.getUserByUsername(usernameFromSecurity);

        List<Chat> userChatList = chatService.getAllChatsByUserId(authenticatedUser.getUserId());

        Boolean isPresent = userChatList.stream()
                .anyMatch(chatInList -> chatInList.getChatId().equals(chatId));
        if(isPresent){
            return new ResponseEntity<Chat>(chatService.getChatById(chatId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    NOT TO BE CALLED BY ORDINARY USERS
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
