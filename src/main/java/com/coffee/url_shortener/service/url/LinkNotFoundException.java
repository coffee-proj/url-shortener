package com.coffee.url_shortener.service.url;

public class LinkNotFoundException extends Exception {
    public LinkNotFoundException(String message) {
        super(message);
    }
}