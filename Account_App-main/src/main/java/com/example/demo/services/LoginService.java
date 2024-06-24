package com.example.demo.services;
import com.example.demo.dao.LoginDAO;
import com.example.demo.dto.LoginDto;
import com.example.demo.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginDAO loginDAO;

    public Login login(LoginDto loginDto) throws Exception {
        if (loginDto == null) {
            throw new Exception("Login DTO is null");
        }
        String username = loginDto.getUserName();
        String password = loginDto.getPassword();
        Login user = loginDAO.findByUserNameAndPassword(username, password);
        if (user == null) {
            throw new Exception("Invalid username or password");
        }
        return user;
    }
}
