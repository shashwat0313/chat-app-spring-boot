package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.*;
import com.chatapp.dev1.Services.ChatService;
import com.chatapp.dev1.Services.ParticipantService;
import com.chatapp.dev1.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/participant")
public class ParticipantController {

    private final ParticipantService participantService;
    private final UserService userService;
    private final ChatService chatService;

    public ParticipantController(ParticipantService participantService, UserService userService, ChatService chatService) {
        this.participantService = participantService;
        this.userService = userService;
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAllPraticipants(){
        return new ResponseEntity<>(participantService.getAllParticipants(), HttpStatus.OK);
    }

    @GetMapping("/{participant_id}")
    public Participant addParticipant(@PathVariable Long participant_id) {
        return participantService.getParticipantById(participant_id);
    }
    
    @PostMapping("/add-participant")
    public ResponseEntity<?> addParticipant(@RequestBody ParticipantDTO participantDTO) {
// unable to receive a participant object because of referenced objects., so will have to receive userid and chatid to process the request
        System.out.println("participant received: \n" + participantDTO.toString());

//      if valid participant
        if( !isValidParticipantDTO(participantDTO) || !chatService.existsByChatChatId(participantDTO.getChatId()) || !userService.existsByUserId(participantDTO.getUserId()) ) {
            return new ResponseEntity<>(new APIResponse(false, "invalid participant info", null), HttpStatus.BAD_REQUEST);
        }

        // Use JPA references to avoid fully retrieving User and Chat from the database
        Chat chat = participantService.getReferenceToChat(participantDTO.getChatId());
        User user = participantService.getReferenceToUser(participantDTO.getUserId());

//        it is now guaranteed that chat exists
        if( !chat.getIsGroup() ){
//            then reject the request if count of users in chat already 2
            int count = participantService.getUserCountByChatRef(chat);
            if(count >= 2){
                return new ResponseEntity<>(new APIResponse(false, "the chat is not a group, hence max 2 users are allowed", null), HttpStatus.BAD_REQUEST);
            }
        }

        Participant participant = Participant.builder()
                .chat(chat)
                .user(user)
                .build();

        Participant savedParticipant = participantService.saveParticipant(participant);
        Long savedParticipantId = savedParticipant.getParticipantId();

        log.info("saved participant details: \nusername: " + savedParticipant.getUser().getUsername() + "\nassociated chat name: " +savedParticipant.getChat().getChatName());
        // Fully initialize lazy-loaded fields

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    private boolean isValidParticipant(Participant participant) {
        return participant.getUser() != null && participant.getChat() != null;
    }

    private boolean isValidParticipantDTO(ParticipantDTO participantDTO) {
        return participantDTO.getUserId() != null && participantDTO.getChatId() != null;
    }
}
