package com.gocle.lxp.tracking.mapper.backend;

import com.gocle.lxp.tracking.domain.LxpApiKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LxpApiKeyMapper {

    /**
     * API Key 문자열로 API Key + Client 정보 조회
     */
    LxpApiKey findByApiKey(@Param("apiKey") String apiKey);
}
