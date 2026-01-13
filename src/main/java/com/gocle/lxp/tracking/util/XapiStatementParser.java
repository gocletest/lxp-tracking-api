package com.gocle.lxp.tracking.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XapiStatementParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private XapiStatementParser() {}

    public static XapiStatementDto parse(String rawJson) {

        XapiStatementDto dto = new XapiStatementDto();
        dto.setRawJson(rawJson);

        try {
            JsonNode root = objectMapper.readTree(rawJson);

            dto.setActorId(extractActorId(root));
            dto.setVerb(extractVerb(root));
            dto.setObjectId(extractObjectId(root));
            dto.setPlatform(extractPlatform(root));

        } catch (Exception e) {
            log.error("[XAPI PARSER] JSON 파싱 실패", e);
        }

        return dto;
    }

    private static String extractActorId(JsonNode root) {
        JsonNode account = root.at("/actor/account/name");
        if (!account.isMissingNode()) return account.asText();

        JsonNode mbox = root.at("/actor/mbox");
        if (!mbox.isMissingNode()) return mbox.asText();

        return "UNKNOWN_ACTOR";
    }

    private static String extractVerb(JsonNode root) {
        JsonNode verb = root.at("/verb/id");
        if (!verb.isMissingNode()) return verb.asText();
        return "UNKNOWN_VERB";
    }

    private static String extractObjectId(JsonNode root) {
        JsonNode obj = root.at("/object/id");
        if (!obj.isMissingNode()) return obj.asText();
        return "UNKNOWN_OBJECT";
    }

    private static String extractPlatform(JsonNode root) {
        JsonNode platform = root.at("/context/platform");
        if (!platform.isMissingNode()) return platform.asText();
        return "UNKNOWN_PLATFORM";
    }
}
