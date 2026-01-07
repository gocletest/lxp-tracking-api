package com.gocle.lxp.tracking.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LearningLog {

    private Long id;
    private String actorId;
    private String verb;
    private String objectId;
    private String platform;
    private String rawJson;
    private LocalDateTime createdAt;
}
