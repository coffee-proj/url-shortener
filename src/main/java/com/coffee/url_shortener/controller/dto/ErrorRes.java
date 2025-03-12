package com.coffee.url_shortener.controller.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JSON with message, timestamp, status of error")
public record ErrorRes(
        @Schema(description = "timestamp time of error", example = "2024-03-11T11:03:34.070440776") String timestamp,
        @Schema(description = "error message with description of the error", example = "Invalid input URL") String message,
        @Schema(description = "status code of the error", example = "BAD_REQUEST") HttpStatus status) {
}
