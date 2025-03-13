package com.coffee.url_shortener.controller.url;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffee.url_shortener.controller.dto.AliasRes;
import com.coffee.url_shortener.controller.dto.ErrorRes;
import com.coffee.url_shortener.controller.dto.FullUrlReq;
import com.coffee.url_shortener.entity.Url;
import com.coffee.url_shortener.service.url.UrlService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/url")
public class UrlController {
    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Success generate alias of URL", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AliasRes.class))),
            @ApiResponse(description = "Invalid input URL", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRes.class, example = """
                    {
                       "timestamp": "2022-03-11T11:16:21.231474959",
                       "message": "Invalid input data",
                       "status": "BAD_REQUEST"
                    }
                    """))) })

    @PostMapping("/new")
    public ResponseEntity<AliasRes> generateAliasUrl(@Valid @RequestBody FullUrlReq input) {
        Url url = new Url();
        url.setFullUrl(input.getFullUrl());

        log.info(url.toString());

        String shortened = service.genAndSaveAlias(url);

        AliasRes res = new AliasRes(shortened);

        return ResponseEntity.ok(res);
    }

}