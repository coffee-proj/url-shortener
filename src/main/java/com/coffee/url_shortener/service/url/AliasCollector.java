package com.coffee.url_shortener.service.url;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.coffee.url_shortener.entity.Url;
import com.coffee.url_shortener.repository.UrlRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableAsync
public class AliasCollector  implements InitializingBean, DisposableBean  {
    private final UrlRepository repository;
    private Thread thread;

    public AliasCollector(UrlRepository repository) {
        this.repository = repository;
    }

    protected void cleanUselessAlias() {
        log.info("AliasCollector starting");
        while (true) {
            List<Url> urls = repository.existsUselessAlias();
            if (!urls.isEmpty()) {
                repository.deleteAll(urls);
            }
            try {
                Thread.sleep(Duration.ofHours(2));
            } catch (Exception e) {
                log.warn(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void destroy() {
        log.info("AliasCollector stopped");
        thread.interrupt();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        thread = Thread.startVirtualThread(() -> {
            cleanUselessAlias();
        });
    }


}