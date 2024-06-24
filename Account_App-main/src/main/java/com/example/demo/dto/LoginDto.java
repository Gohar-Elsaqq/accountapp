package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LoginDto {
    private String userName;
    private String password;
}
