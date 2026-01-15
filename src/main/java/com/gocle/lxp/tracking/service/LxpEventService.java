package com.gocle.lxp.tracking.service;

import com.gocle.lxp.tracking.dto.XapiStatementDto;
import jakarta.servlet.http.HttpServletRequest;

public interface LxpEventService {

    /**
     * LXP 이벤트 저장
     * - Tracking DB
     * - Elasticsearch
     */
    void save(XapiStatementDto dto, HttpServletRequest request);
}
