package com.gocle.lxp.tracking.mapper.backend;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApiKeyUsageMapper {

    /**
     * 최근 1분간 API Key 사용 횟수
     */
    int countUsageLastMinute(@Param("apiKeyId") Long apiKeyId);

    /**
     * API Key 사용 로그 저장
     */
    int insertUsage(
            @Param("apiKeyId") Long apiKeyId,
            @Param("apiKey") String apiKey,
            @Param("endpoint") String endpoint,
            @Param("ipAddress") String ipAddress
    );
}
