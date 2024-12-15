package com.chatapp.dev1.Services;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.Participant;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Repositories.ChatRepository;
import com.chatapp.dev1.Repositories.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ParticipantService participantService;
    private final ParticipantRepository participantRepository;

    public ChatService(ChatRepository chatRepository, ParticipantService participantService, UserService userService, ParticipantRepository participantRepository) {
        this.chatRepository = chatRepository;
        this.participantService = participantService;
        this.userService = userService;
        this.participantRepository = participantRepository;
    }

    public List<Chat> getAllChats(){
        return chatRepository.findAll();
    }

    public Chat getChatById(Long chatId){
        return chatRepository.findById(chatId).orElse(null);
    }

    public Chat saveChat(Chat chat){
        return chatRepository.save(chat);
    }

    public void deleteChatById(Long chatId){
        chatRepository.deleteById(chatId);
    }

    public List<Chat> getAllChatsByUserId(Long userId) {
//      user -> participant -> chats
        User user = userService.getUserByUserId(userId);

        List<Participant> userParticipations = participantService.getParticipantsByUserId(user);

//      from each participant object linked to the user, get each corresponding chat object
        List<Chat> chats = new java.util.ArrayList<>(List.of());
        
        for(int i = 0; i < userParticipations.size(); i++){ 
            Participant participant = userParticipations.get(i);
            Chat chat = participant.getChat();
            chats.add(chat);
        }

        return userParticipations.stream().map(Participant::getChat).toList();
    }

    public boolean existsByChatChatId(Long chatId){
        return chatRepository.existsById(chatId);
    }
}
