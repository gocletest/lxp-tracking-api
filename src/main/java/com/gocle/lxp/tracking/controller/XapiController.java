package com.gocle.lxp.tracking.controller;

import com.gocle.lxp.tracking.common.ApiResponse;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import com.gocle.lxp.tracking.service.XapiService;
import com.gocle.lxp.tracking.util.XapiStatementParser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xapi")
@RequiredArgsConstructor
public class XapiController {

    private final XapiService xapiService;

    @PostMapping("/statement")
    public ApiResponse<?> receiveStatement(@RequestBody String rawJson) {

        // 1. raw JSON → DTO
        XapiStatementDto dto = XapiStatementParser.parse(rawJson);

        // 2. 저장
        xapiService.saveStatement(dto);

        return ApiResponse.success("xAPI 수신 및 저장 완료", null);
    }
}
