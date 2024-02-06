package com.davo.backend.exception;

import java.time.LocalDateTime;

public class ErrorDetails {

    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorDetails() {
    }

    public ErrorDetails(String error, String message, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }
}
