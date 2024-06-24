package com.example.demo.controller;

import com.example.demo.configurationController.BaseController;
import com.example.demo.dto.LoginDto;
import com.example.demo.entity.Login;
import com.example.demo.services.LoginService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
public class LoginController extends BaseController {
    @Autowired
    private LoginService loginService;
    @PostMapping(value = "/login")
    public Login login(@RequestBody LoginDto loginDto) throws Exception {
        return loginService.login(loginDto);
    }
}
