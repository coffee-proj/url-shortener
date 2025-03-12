package com.coffee.url_shortener.controller.dto;

import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(description = "Full link for generate short", contentMediaType = "application/json")
public class FullUrlReq {
    @NotNull
    @URL(message = "Invalid input URL")
    @Schema(description = "Full url", example = "https://google.com/", requiredMode = RequiredMode.REQUIRED)
    private String fullUrl;

}
