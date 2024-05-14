package com.example.demo.configuration;

import com.example.demo.dto.BaseDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@CommonsLog
public class  BaseService extends BaseDTO implements Serializable {
    public static final String SUCCESS = "Success";
    public static final String YYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


}
