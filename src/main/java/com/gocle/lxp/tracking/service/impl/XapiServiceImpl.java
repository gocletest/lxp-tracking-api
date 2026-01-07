package com.gocle.lxp.tracking.service.impl;

import com.gocle.lxp.tracking.domain.LearningLog;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import com.gocle.lxp.tracking.mapper.LearningLogMapper;
import com.gocle.lxp.tracking.service.ElasticsearchService;
import com.gocle.lxp.tracking.service.XapiService;
import com.gocle.lxp.tracking.util.VerbNormalizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class XapiServiceImpl implements XapiService {

    private final LearningLogMapper learningLogMapper;
    private final ElasticsearchService elasticsearchService;

    @Override
    public void saveStatement(XapiStatementDto dto) {

        // 1. DTO → Domain 변환
        LearningLog log = new LearningLog();
        log.setActorId(dto.getActorId());
        log.setVerb(VerbNormalizer.normalize(dto.getVerb()));
        log.setObjectId(dto.getObjectId());
        log.setPlatform(dto.getPlatform());
        log.setRawJson(dto.getRawJson());

        // 2. MariaDB 저장 (필수)
        learningLogMapper.insert(log);

        // 3. Elasticsearch 저장 (옵션)
        elasticsearchService.indexLearningLog(
                log.getActorId(),
                log.getVerb(),
                log.getObjectId(),
                log.getPlatform(),
                log.getRawJson()
        );
    }
}
