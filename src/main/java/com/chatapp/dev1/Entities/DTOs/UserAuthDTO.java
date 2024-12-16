package com.chatapp.dev1.Entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthDTO {
    Long id;
    String username;
    String token;
    String message;
}
