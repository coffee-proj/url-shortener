package com.coffee.url_shortener.service.url;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.imageio.ImageIO;

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
            url.setAlias(AliasGenerator.generateAlias(url.getUrl()));
        } while (repository.existsByAlias(url.getAlias()));

        url = repository.save(url);

        return url.getAlias();
    }

    @Transactional
    @Cacheable(value = "alias")
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

    public byte[] genQrCodeByUrl(String url) throws IllegalArgumentException, IOException {
        BufferedImage image = AliasGenerator.generateQrcode(url);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        
        return imageBytes;
    }
}