package com.gocle.lxp.tracking.security;

/**
 * Tracking API - API Key 인증/인가 예외
 */
public class ApiKeyException extends RuntimeException {

    private final int statusCode;

    public ApiKeyException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiKeyException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
