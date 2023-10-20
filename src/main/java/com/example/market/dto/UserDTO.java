package com.example.market.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String password; // Note: never store plain-text passwords in production applications
    private String email;

    // constructors, standard getters and setters
}
