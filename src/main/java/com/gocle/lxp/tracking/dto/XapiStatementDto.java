package com.gocle.lxp.tracking.dto;

import lombok.Data;

@Data
public class XapiStatementDto {
	private String actorId;
    private String verb;
    private String objectId;
    private String platform;
    private String rawJson;
}
