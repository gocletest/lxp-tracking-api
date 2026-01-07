package com.gocle.lxp.tracking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${elasticsearch.index.learning-log}")
    private String learningLogIndex;

    public void indexLearningLog(
            String actorId,
            String verb,
            String objectId,
            String platform,
            String rawJson
    ) {
    	
    	  // 이 로그가 찍히는지부터 확인
        log.info("[ES] indexLearningLog called. index={}", learningLogIndex);
    	
        try {
            Map<String, Object> doc = new HashMap<>();
            doc.put("actor_id", actorId);
            doc.put("verb", verb);
            doc.put("object_id", objectId);
            doc.put("platform", platform);
            doc.put("created_at", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
            doc.put("raw_json", rawJson);

            Request request = new Request(
                    "POST",
                    "/" + learningLogIndex + "/_doc"
            );

            request.setJsonEntity(
                    objectMapper.writeValueAsString(doc)
            );

            restClient.performRequest(request);

        } catch (Exception e) {
            // ❗ Tracking API 핵심 정책
            log.error("[ES] learning_log index 실패", e);
        }
    }
}
