package com.sw.newProject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseTimeEntity {

    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
