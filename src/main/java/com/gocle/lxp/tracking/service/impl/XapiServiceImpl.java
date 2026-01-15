package com.gocle.lxp.tracking.service.impl;

import com.gocle.lxp.tracking.domain.LearningLog;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import com.gocle.lxp.tracking.mapper.tracking.LearningLogMapper;
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

        LearningLog log = new LearningLog();
        log.setActorId(dto.getActorId());
        log.setVerb(VerbNormalizer.normalize(dto.getVerb()));
        log.setObjectId(dto.getObjectId());
        log.setPlatform(dto.getPlatform());
        log.setRawJson(dto.getRawJson());

		/*
		 * // 1. DB learningLogMapper.insert(log);
		 * 
		 * // 2. ES elasticsearchService.indexLearningLog( log.getActorId(),
		 * log.getVerb(), log.getObjectId(), log.getPlatform(), log.getRawJson() );
		 */
    }
}
