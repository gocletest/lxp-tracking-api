package com.gocle.lxp.tracking.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XapiStatementParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private XapiStatementParser() {
        // util class
    }

    public static XapiStatementDto parse(String rawJson) {

        XapiStatementDto dto = new XapiStatementDto();
        dto.setRawJson(rawJson);

        try {
            JsonNode root = objectMapper.readTree(rawJson);

            // 1️. actorId
            dto.setActorId(extractActorId(root));

            // 2️. verb
            dto.setVerb(extractVerb(root));

            // 3️. objectId
            dto.setObjectId(extractObjectId(root));

            // 4️. platform
            dto.setPlatform(extractPlatform(root));

        } catch (Exception e) {
            // ❗ 파싱 실패해도 DTO는 반환
            log.error("[XAPI PARSER] JSON 파싱 실패", e);
        }

        return dto;
    }

    /* =======================
       이하 세부 추출 메서드
       ======================= */

    private static String extractActorId(JsonNode root) {

        // actor.account.name (우선)
        JsonNode accountName = root.at("/actor/account/name");
        if (!accountName.isMissingNode()) {
            return accountName.asText();
        }

        // actor.mbox (fallback)
        JsonNode mbox = root.at("/actor/mbox");
        if (!mbox.isMissingNode()) {
            return mbox.asText();
        }

        return "UNKNOWN_ACTOR";
    }

    private static String extractVerb(JsonNode root) {

        // verb.display.en-US
        JsonNode verbDisplay = root.at("/verb/display/en-US");
        if (!verbDisplay.isMissingNode()) {
            return verbDisplay.asText();
        }

        // verb.id (fallback)
        JsonNode verbId = root.at("/verb/id");
        if (!verbId.isMissingNode()) {
            return verbId.asText();
        }

        return "UNKNOWN_VERB";
    }

    private static String extractObjectId(JsonNode root) {

        // object.id
        JsonNode objectId = root.at("/object/id");
        if (!objectId.isMissingNode()) {
            return objectId.asText();
        }

        return "UNKNOWN_OBJECT";
    }

    private static String extractPlatform(JsonNode root) {

        // context.platform
        JsonNode platform = root.at("/context/platform");
        if (!platform.isMissingNode()) {
            return platform.asText();
        }

        return "UNKNOWN_PLATFORM";
    }
}
