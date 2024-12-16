package com.chatapp.dev1.Entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private boolean status;
    private String token;
    private String message;
}
