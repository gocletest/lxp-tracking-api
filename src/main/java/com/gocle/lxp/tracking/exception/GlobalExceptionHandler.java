package com.gocle.lxp.tracking.exception;

import com.gocle.lxp.tracking.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. NullPointerException, IllegalArgumentException 등 공통 예외 처리
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ApiResponse<?> handleBadRequest(Exception e) {
        return ApiResponse.error(400, e.getMessage());
    }

    // 2. Validation 실패 (추후 DTO 유효성 검사 시 발생)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ApiResponse.error(400, errorMessage);
    }

    //  3. 그 외 모든 예외 처리 (서버 에러)
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        e.printStackTrace(); // 서버 로그 출력
        return ApiResponse.error(500, "서버 오류가 발생했습니다.");
    }
}
