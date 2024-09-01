package com.araouf.server.exceptions;

import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorResponse {
    private Object message; // Can be a String or a Map
    private HttpStatus status;
    private Integer statusCode;
    private ZonedDateTime timestamp;
    private String path;

    public ErrorResponse(String message, HttpStatus status, Integer statusCode,String path, ZonedDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.path = path;
    }

    public ErrorResponse(Map<String, String> message, HttpStatus status, Integer statusCode, String path,ZonedDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.path = path;
    }
}
