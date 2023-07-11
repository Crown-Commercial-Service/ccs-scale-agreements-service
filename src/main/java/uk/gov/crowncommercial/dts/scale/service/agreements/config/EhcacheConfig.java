package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

/**
 * Ehcache cache setup and configuration for the application
 */
@Configuration
public class EhcacheConfig {
    @Value("${caching.primary.cacheLength}")
    String primaryCacheLength;

    @Value("${caching.secondary.cacheLength}")
    String secondaryCacheLength;

    /**
     * Initialise the caches we want to use based on life configuration settings
     */
    @Bean
    public CacheManager ehCacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        javax.cache.configuration.Configuration<String, Object> primaryCacheConfig = getCacheConfigForSpecifiedLifespan(primaryCacheLength);
        javax.cache.configuration.Configuration<String, Object> secondaryCacheConfig = getCacheConfigForSpecifiedLifespan(secondaryCacheLength);

        // Establish primary caches
        cacheManager.createCache("getAgreementsList", primaryCacheConfig);
        cacheManager.createCache("getAgreementDetail", primaryCacheConfig);
        cacheManager.createCache("getLotsForAgreement", primaryCacheConfig);
        cacheManager.createCache("getDocumentsForAgreement", primaryCacheConfig);
        cacheManager.createCache("getUpdatesForAgreement", primaryCacheConfig);
        cacheManager.createCache("getLotDetail", primaryCacheConfig);
        cacheManager.createCache("getLotEventTypes", primaryCacheConfig);
        cacheManager.createCache("getEventDataTemplates", primaryCacheConfig);
        cacheManager.createCache("getEventDocumentTemplates", primaryCacheConfig);

        // Establish secondary caches
        cacheManager.createCache("getLotSuppliers", secondaryCacheConfig);

        return cacheManager;
    }

    /**
     * Builds a cache configuration object based on the specified life in seconds passed to it
     */
    private javax.cache.configuration.Configuration<String, Object> getCacheConfigForSpecifiedLifespan(String cacheLength) {
        CacheConfigurationBuilder<String, Object> cacheConfigBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                Object.class,
                                ResourcePoolsBuilder
                                        .newResourcePoolsBuilder().heap(50))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(Integer.parseInt(cacheLength))));

        javax.cache.configuration.Configuration<String, Object> cacheConfig = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfigBuilder);

        return cacheConfig;
    }
}