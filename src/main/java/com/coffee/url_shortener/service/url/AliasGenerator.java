package com.coffee.url_shortener.service.url;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AliasGenerator {
    public static String generateAlias(String fullUrl) {
        List<String> t = Arrays.asList(fullUrl.concat(UUID.randomUUID().toString()).split(""));
        Collections.shuffle(t);

        String s = "";
        s = String.join(s, t);
        
        return Base64.getEncoder().withoutPadding().encodeToString(s.getBytes()).substring(0, 6);
    }
}
