package com.gocle.lxp.tracking.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LxpApiKey {

    /* ===============================
     * lxp_api_key
     * =============================== */

    /** API Key 고유 ID */
    private Long apiKeyId;

    /** 발급된 API Key 문자열 */
    private String apiKey;

    /** 허용 도메인 (comma-separated 또는 JSON) */
    private String allowedDomains;

    /** 활성화 여부 */
    private boolean enabled;

    /** 분당 호출 제한 */
    private Integer rateLimitPerMin;

    /** 만료 일시 */
    private LocalDateTime expiresAt;

    /** 생성 일시 (선택) */
    private LocalDateTime createdAt;

    /** 소속 Client ID */
    private Long clientId;

    /* ===============================
     * lxp_api_client (JOIN)
     * =============================== */

    /** Client 상태 (ACTIVE / INACTIVE) */
    private String clientStatus;

    /** Client 코드 (선택, 로그/분석용) */
    private String clientCode;

    /** Client 명칭 (선택) */
    private String clientName;
}
