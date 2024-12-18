package com.chatapp.dev1.Entities.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTO_Receive {
    String chatId;
    String text;
}
