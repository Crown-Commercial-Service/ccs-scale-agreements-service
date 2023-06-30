package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Ehcache cache setup and configuration for the application
 */
@Configuration
public class EhcacheConfig {
    @Value("${caching.spring.primary.cacheLength}")
    String primaryCacheLength;

    @Value("${caching.spring.secondary.cacheLength}")
    String secondaryCacheLength;

    /**
     * Initialise the caches we want to use based on life configuration settings
     */
    @Bean
    public CacheManager ehCacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        CacheConfigurationBuilder<String, Object> primaryCacheConfigBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                Object.class,
                                ResourcePoolsBuilder
                                        .newResourcePoolsBuilder().offheap(100, MemoryUnit.MB))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(Integer.parseInt(primaryCacheLength))));

        javax.cache.configuration.Configuration<String, Object> primaryCacheConfig = Eh107Configuration.fromEhcacheCacheConfiguration(primaryCacheConfigBuilder);

        // Establish primary caches
        cacheManager.createCache("getAgreementsList", primaryCacheConfig);
        cacheManager.createCache("getAgreementDetail", primaryCacheConfig);
        cacheManager.createCache("getLotsForAgreement", primaryCacheConfig);
        cacheManager.createCache("getDocumentsForAgreement", primaryCacheConfig);
        cacheManager.createCache("getUpdatesForAgreement", primaryCacheConfig);

        // Establish secondary caches

        return cacheManager;

    }

}