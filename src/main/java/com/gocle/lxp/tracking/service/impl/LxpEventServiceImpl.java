package com.gocle.lxp.tracking.service.impl;

import com.gocle.lxp.tracking.domain.LearningLog;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import com.gocle.lxp.tracking.mapper.tracking.LearningLogMapper;
import com.gocle.lxp.tracking.service.ElasticsearchService;
import com.gocle.lxp.tracking.service.LxpEventService;
import com.gocle.lxp.tracking.util.VerbNormalizer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LxpEventServiceImpl implements LxpEventService {

    private final LearningLogMapper learningLogMapper;
    private final ElasticsearchService elasticsearchService;

    @Override
    public void save(XapiStatementDto dto, HttpServletRequest request) {
    	
    	// ApiKeyFilter에서 주입된 apiKeyId , clientId
        Long apiKeyId 	= (Long) request.getAttribute("apiKeyId");
        Long clientId 	= (Long) request.getAttribute("clientId");

        // 1. Tracking DB 저장
        LearningLog log = new LearningLog();
        log.setApiKeyId(apiKeyId); 
        log.setActorId(dto.getActorId());
        log.setVerb(VerbNormalizer.normalize(dto.getVerb()));
        log.setObjectId(dto.getObjectId());
        log.setPlatform(dto.getPlatform());
        log.setRawJson(dto.getRawJson());
        
        learningLogMapper.insert(log);
        
        // 2. Elasticsearch 적재
        elasticsearchService.indexLearningLog(
                log.getActorId(),
                log.getVerb(),
                log.getObjectId(),
                log.getPlatform(),
                log.getRawJson(),
                apiKeyId,
                clientId
        );
    }
}
