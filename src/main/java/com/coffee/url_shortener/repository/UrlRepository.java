package com.coffee.url_shortener.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coffee.url_shortener.entity.Url;

public interface UrlRepository extends JpaRepository<Url, UUID> {
    boolean existsByAlias(String alias);

    Optional<Url> getByAlias(String alias);
}
