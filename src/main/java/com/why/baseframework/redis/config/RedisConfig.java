package com.why.baseframework.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.why.baseframework.constants.IntConstants;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author W
 * @Description:
 * @Title: RedisConfig
 * @ProjectName WHY-Core
 * @Date 2021/4/16 10:33
 * @Company  WHY-Group
 */
@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class RedisConfig {
    /**
     * 配置简单的redisTemplate序列化器
     *
     * @param redisConnectionFactory redis的连接工厂对象
     * @return RedisTemplate<String, Object> redisTemplate
     * @author W
     * @date: 2021/4/16
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> strKeyRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        return getRedisTemplate(redisConnectionFactory, template);
    }

    /**
     * 配置以Object对象为key的redisTemplate序列化器
     *
     * @param connectionFactory redis的连接工厂对象
     * @return RedisTemplate<Object, Object>
     * @author W
     * @date: 2021/4/25
     */
    @Bean("objTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        return getRedisTemplate(connectionFactory, template);
    }

    /**
     * 设置redisTemplate的序列化器
     *
     * @param connectionFactory redis连接工厂
     * @param template          redisTemplate对象
     * @return void
     * @author W
     * @date: 2021/4/25
     */
    private <T> RedisTemplate<T, Object> getRedisTemplate(RedisConnectionFactory connectionFactory, RedisTemplate<T, Object> template) {
        template.setConnectionFactory(connectionFactory);
        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(StringRedisSerializer.UTF_8);
        template.setHashKeySerializer(StringRedisSerializer.UTF_8);
        return template;
    }

    /**
     * 普通的redisTemplate的缓存设置
     *
     * @param template redisTemplate对象
     * @return CacheManager 缓存管理器
     * @author W
     * @date: 2021/4/16
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> template) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ZERO)
                // 设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getStringSerializer()))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getValueSerializer()))
                // 不缓存空值
                .disableCachingNullValues();

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("1m", config.entryTtl(Duration.ofMinutes(IntConstants.INT_1)));
        configMap.put("5m", config.entryTtl(Duration.ofMinutes(IntConstants.INT_5)));
        configMap.put("15m", config.entryTtl(Duration.ofMinutes(IntConstants.INT_15)));
        configMap.put("30m", config.entryTtl(Duration.ofMinutes(IntConstants.INT_30)));
        configMap.put("1h", config.entryTtl(Duration.ofHours(IntConstants.INT_1)));
        configMap.put("2h", config.entryTtl(Duration.ofHours(IntConstants.INT_2)));
        configMap.put("4h", config.entryTtl(Duration.ofHours(IntConstants.INT_4)));
        configMap.put("24h", config.entryTtl(Duration.ofHours(IntConstants.INT_24)));
        configMap.put("1d", config.entryTtl(Duration.ofDays(IntConstants.INT_1)));
        configMap.put("30d", config.entryTtl(Duration.ofDays(IntConstants.INT_30)));

        // 使用自定义的缓存配置初始化一个cacheManager
        return RedisCacheManager.builder(Objects.requireNonNull(template.getConnectionFactory()))
                .cacheDefaults(config)
                // 一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(configMap.keySet())
                .withInitialCacheConfigurations(configMap)
                .build();
    }
}
