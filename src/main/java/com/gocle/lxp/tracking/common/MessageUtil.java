package com.gocle.lxp.tracking.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String get(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }
}
