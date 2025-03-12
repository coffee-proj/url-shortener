package com.coffee.url_shortener.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Alias for redirect to remote URL", contentMediaType = "application/json")
public class AliasRes {
    @Schema(description = "Field of alias", maxLength = 6, minLength = 6, example = "Nm06dG")
    private String alias;
}
