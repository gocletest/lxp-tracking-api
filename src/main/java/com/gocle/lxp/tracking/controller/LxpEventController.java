package com.gocle.lxp.tracking.controller;

import com.gocle.lxp.tracking.common.ApiResponse;
import com.gocle.lxp.tracking.dto.XapiStatementDto;
import com.gocle.lxp.tracking.service.LxpEventService;
import com.gocle.lxp.tracking.service.XapiService;
import com.gocle.lxp.tracking.util.XapiStatementParser;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lxp")
@RequiredArgsConstructor
public class LxpEventController {

    private final LxpEventService lxpEventService;

    @PostMapping("/events")
    public ApiResponse<?> receiveEvent(
            @RequestBody String rawJson,
            HttpServletRequest request
    ) {
        XapiStatementDto dto = XapiStatementParser.parse(rawJson);

        lxpEventService.save(dto, request);

        return ApiResponse.success("LXP event received", null);
    }
}


