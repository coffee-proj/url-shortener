package com.coffee.url_shortener.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import com.coffee.url_shortener.entity.Url;



public interface UrlRepository extends JpaRepository<Url, UUID> {
    boolean existsByAlias(String alias);


    @Query(value = """
        SELECT * FROM urls WHERE read_at < localtimestamp - INTERVAL '1 week'
        """, nativeQuery=true)
    List<Url> existsUselessAlias();
    
    void deleteAll(@NonNull Iterable<? extends Url> entities);

    Optional<Url> getByAlias(String alias);
}
