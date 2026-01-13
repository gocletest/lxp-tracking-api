package com.gocle.lxp.tracking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LxpEventDto {

    private User user = new User();
    private Target target = new Target();
    private Context context = new Context();

    private String eventType;
    private String timestamp;

    /** 🔥 원본 이벤트(JSON) 보존 */
    private String rawJson;

    // ===== Inner DTOs =====

    @Getter @Setter
    public static class User {
        private String id;
    }

    @Getter @Setter
    public static class Target {
        private String id;
    }

    @Getter @Setter
    public static class Context {
        private String lmsId;
    }
}
