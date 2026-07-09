package com.sanosysalvos.ms_registro_mascotas.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Habilita el soporte de caché declarativa (@Cacheable, @CacheEvict, @CachePut).
 * El proveedor real (Redis) se configura vía application.yml -> spring.cache.type=redis.
 *
 * Se usa serialización JSON (GenericJackson2JsonRedisSerializer) en vez de la
 * serialización Java por defecto, para que las entidades no necesiten implementar
 * Serializable y para poder inspeccionar los valores cacheados desde RedisInsight.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        RedisCacheConfiguration jsonCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return builder -> builder.cacheDefaults(jsonCacheConfig);
    }
}
