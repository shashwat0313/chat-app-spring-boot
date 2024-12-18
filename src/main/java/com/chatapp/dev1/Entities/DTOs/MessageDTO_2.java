package com.chatapp.dev1.Entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDTO_2 {
    String sender;
    String text;
    LocalDateTime sentAt;
}
