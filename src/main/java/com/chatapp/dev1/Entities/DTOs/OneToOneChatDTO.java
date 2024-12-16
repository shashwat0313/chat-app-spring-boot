package com.chatapp.dev1.Entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OneToOneChatDTO {
    String chatName;
    String friendUsername;
}
