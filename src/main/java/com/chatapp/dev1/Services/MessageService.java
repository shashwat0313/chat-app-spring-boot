package com.chatapp.dev1.Services;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.Message;
import com.chatapp.dev1.Repositories.ChatRepository;
import com.chatapp.dev1.Repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(Long messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public void deleteMessageById(Long messageId){
        messageRepository.deleteById(messageId);
    }

    public List<Message> getAllMessagesByChatId(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        return messageRepository.findAllByChat(chat);
    }
}
