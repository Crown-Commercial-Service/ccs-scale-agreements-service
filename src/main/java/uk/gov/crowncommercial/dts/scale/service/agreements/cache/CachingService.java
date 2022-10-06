package uk.gov.crowncommercial.dts.scale.service.agreements.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class CachingService {

    @Autowired
    CacheManager cacheManager;

    @Scheduled(fixedDelayString = "PT1M")
    public void clearAllCaches() {
        evictAllCaches();
    }
    private void evictAllCaches() {

        log.info("Clearing cache : Started");
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(cacheName -> {

                    if( Objects.nonNull(cacheManager.getCache(cacheName))){

                        cacheManager.getCache(cacheName).clear();
                    }
                });
        log.info("Clearing cache : Completed");

    }
}
