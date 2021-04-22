package com.sample.session;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSession {
    private LocalDateTime created;
    private LocalDateTime lastTouched;
    private String username;
}
