package com.fxdata.notificationservice.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {

        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;

        private ApiError() {
            timestamp = LocalDateTime.now();
        }

        ApiError(HttpStatus status, Throwable ex) {
            this();
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = ex.getMessage();
        }
}
