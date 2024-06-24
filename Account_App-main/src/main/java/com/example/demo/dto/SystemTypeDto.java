package com.example.demo.dto;

import com.example.demo.entity.SystemType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
;

@Getter
@Setter
@Data
public class SystemTypeDto {

    private int id;
    private String lookupType;
    private String status;

    public static SystemTypeDto mapToSystemTypeDto(SystemType systemType) {
        SystemTypeDto systemTypeDto = new SystemTypeDto();
        systemTypeDto.setId(systemType.getId());
        systemTypeDto.setLookupType(systemType.getLookupType());
        systemTypeDto.setStatus(systemType.getStatus());
        return systemTypeDto;
    }
}



