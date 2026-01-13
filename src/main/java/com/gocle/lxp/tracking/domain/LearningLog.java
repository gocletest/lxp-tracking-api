package com.gocle.lxp.tracking.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LXP 공통 학습 로그 (xAPI normalized)
 * - LMS 이벤트 → xAPI Adapter → LearningLog
 * - Analytics / AI 기준 테이블
 */
@Getter
@Setter
public class LearningLog {

    private Long id;

    /** 사용자 식별자 */
    private String actorId;

    /** xAPI verb (started, completed, etc.) */
    private String verb;

    /** 대상 객체 ID (courseId, contentId, quizId) */
    private String objectId;

    /** LMS 또는 플랫폼 식별자 */
    private String platform;

    /** 원본 LMS 이벤트 JSON */
    private String rawJson;

    /** 이벤트 발생 시각 */
    private LocalDateTime createdAt;

    // ⬇⬇⬇ 지금은 안 써도 됨 (확장 대비)
    // private String objectType; // COURSE, CONTENT, QUIZ
    // private String result;     // score, success 등
}

