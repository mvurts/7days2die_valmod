package com.zombie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mvurts on 27.04.2018
 */
@Slf4j
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
@SpringBootApplication
@ComponentScan("com.zombie")
public class Application {

    @Autowired
    private Environment environment;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Caches configuration JSR-107
     */
    @Bean
    public JCacheManagerCustomizer initCaches() {
        String[] cacheNames = {"items"};
        final Map<String, Integer> cacheSecondsMap = new HashMap<>();
        for (String name : cacheNames) {
            Integer timeToLive = environment.getProperty("zombie.cache." + name + ".secondsToLive", Integer.class);
            if (timeToLive == null) {
                timeToLive = 15;
            } else if (timeToLive < 3) {
                timeToLive = 3;
            }
            cacheSecondsMap.put(name, timeToLive);
        }
        return (cacheManager) -> {
            for (Map.Entry<String, Integer> entry : cacheSecondsMap.entrySet()) {
                cacheManager.createCache(entry.getKey(), new MutableConfiguration<>()
                        .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, entry.getValue())))
                        .setStoreByValue(false)
                        .setStatisticsEnabled(true)
                );
            }
        };
    }

}
