package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.annotations.CreationTimestamp;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
@Setter
@Getter
@CommonsLog
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7681337134969543194L;

    @CreationTimestamp
    @Column(name = "CREATION_TIME")
    private LocalDateTime creationTime;
}
