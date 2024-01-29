package com.example.demo.configuration;

import com.example.demo.dto.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import java.io.Serializable;
@Getter
@CommonsLog
public  final class Utility extends BaseDTO implements Serializable {
    @Getter
    public static Gson gson;
    public static final String SUCCESS = "Success";
    private String status;
    private String description;
    private String errorCode;
    private String errorMessage;
    public Utility() {

    }

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }
    public Utility(String status, String description) {
        this.status = status;
        this.description = description;
    }

}

