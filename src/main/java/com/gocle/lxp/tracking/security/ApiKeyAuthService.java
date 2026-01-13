package com.gocle.lxp.tracking.security;

import com.gocle.lxp.tracking.domain.LxpApiKey;
import com.gocle.lxp.tracking.mapper.ApiKeyUsageMapper;
import com.gocle.lxp.tracking.mapper.LxpApiKeyMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ApiKeyAuthService {

    private final LxpApiKeyMapper apiKeyMapper;
    private final ApiKeyUsageMapper usageMapper;

    /**
     * API Key 인증 및 정책 검증
     */
    public LxpApiKey authenticate(String apiKey, HttpServletRequest request) {

        // 1. API Key 존재 여부
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiKeyException(401, "API Key required");
        }

        // 2. API Key 조회
        LxpApiKey key = apiKeyMapper.findByApiKey(apiKey);
        if (key == null || !key.isEnabled()) {
            throw new ApiKeyException(403, "Invalid or disabled API Key");
        }

        // 3. Client 상태 체크
        if (!"ACTIVE".equals(key.getClientStatus())) {
            throw new ApiKeyException(403, "Client is inactive");
        }

        // 4. 만료 체크
        if (key.getExpiresAt() != null &&
            key.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ApiKeyException(403, "API Key expired");
        }

        // 5. 도메인 체크
        if (!isAllowedDomain(request, key.getAllowedDomains())) {
            throw new ApiKeyException(403, "Domain not allowed");
        }

        // 6. Rate limit 체크
        validateRateLimit(key);

        // 7. 사용 로그 저장
        saveUsage(key, request);

        return key;
    }

    /**
     * 분당 호출 제한 체크
     */
    private void validateRateLimit(LxpApiKey key) {
        int count = usageMapper.countUsageLastMinute(key.getApiKeyId());
        if (count >= key.getRateLimitPerMin()) {
            throw new ApiKeyException(429, "Rate limit exceeded");
        }
    }

    /**
     * API 사용 로그 저장
     */
    private void saveUsage(LxpApiKey key, HttpServletRequest request) {
        usageMapper.insertUsage(
            key.getApiKeyId(),
            key.getApiKey(),
            request.getRequestURI(),
            request.getRemoteAddr()
        );
    }

    /**
     * 허용 도메인 체크
     */
    private boolean isAllowedDomain(HttpServletRequest request, String allowedDomains) {

        if (allowedDomains == null || allowedDomains.isBlank()) {
            return true; // 제한 없음
        }

        String origin = request.getHeader("Origin");
        if (origin == null || origin.isBlank()) {
            return true; // 서버 사이드 호출 허용
        }

        return Arrays.stream(allowedDomains.split(","))
                .map(String::trim)
                .anyMatch(domain ->
                        origin.equals(domain) || origin.endsWith(domain)
                );
    }
}
