package com.coffee.url_shortener.controller.url;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.coffee.url_shortener.controller.dto.ErrorRes;
import com.coffee.url_shortener.entity.Url;
import com.coffee.url_shortener.service.url.LinkNotFoundException;
import com.coffee.url_shortener.service.url.UrlService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class RedirectController {
    private final UrlService service;

    public RedirectController(UrlService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(description = "redirect to URL by alias", content = @Content(examples = {}), responseCode = "302"),
            @ApiResponse(description = "Not found alias", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRes.class, example = """
                    {
                        "timestamp": "2025-03-11T11:10:17.213248598",
                        "message": "Alias not found",
                        "status": "NOT_FOUND"
                        }
                       \s""")))
    })
    @GetMapping("/{alias}")
    @CrossOrigin
    public RedirectView redirect(@PathVariable String alias) throws LinkNotFoundException {
        Url url = service.getFullUrlByAlias(alias);

        return new RedirectView(url.getUrl());
    }
}
