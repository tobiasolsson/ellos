package org.ellos.ellos.exception;

import java.time.Instant;

public class ApiError {
    private Instant timestamp;
    private int statusCode;
    private String message;
    private String description;

    public ApiError(Instant timestamp, int statusCode, String message, String description) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.message = message;
        this.description = description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
