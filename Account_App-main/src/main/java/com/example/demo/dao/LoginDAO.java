package com.example.demo.dao;

import com.example.demo.dto.LoginDto;
import com.example.demo.entity.Login;
import org.apache.juli.logging.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDAO extends JpaRepository<Login, Long> {
    Login findByUserNameAndPassword(String userName, String password);
}