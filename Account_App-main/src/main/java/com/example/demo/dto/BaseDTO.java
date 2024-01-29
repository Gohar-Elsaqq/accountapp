package com.example.demo.dto;

import com.example.demo.configuration.Utility;

import java.io.Serializable;

public class BaseDTO implements Serializable {
    @Override
    public String toString() {

        return Utility.getGson().toJson(this);
    }
}
