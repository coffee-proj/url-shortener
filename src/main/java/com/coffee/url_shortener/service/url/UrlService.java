package com.coffee.url_shortener.service.url;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.coffee.url_shortener.entity.Url;
import com.coffee.url_shortener.repository.UrlRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UrlService {

    private final UrlRepository repository;

    public UrlService(UrlRepository repository) {
        this.repository = repository;

    }

    public String genAndSaveAlias(Url url) {
        do {
            url.setAlias(AliasGenerator.generateAlias(url.getFullUrl()));
        } while (repository.existsByAlias(url.getAlias()));

        url = repository.save(url);

        return url.getAlias();
    }

    @Transactional
    @Cacheable(value = "urls")
    public Url getFullUrlByAlias(String shortened) throws LinkNotFoundException {
        Optional<Url> url = repository.getByAlias(shortened);
        if (url.isEmpty()) {
            throw new LinkNotFoundException("Url not found");
        }

        Url res = url.get();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        res.setReadAt(timestamp);
        repository.save(res);

        return res;
    }
}