package com.chatapp.dev1.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse {
    private boolean success;
    private String message;
    private Object data;
}
