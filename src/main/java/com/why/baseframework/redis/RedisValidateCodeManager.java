package com.why.baseframework.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * @author H
 */
@Slf4j
@Component
public class RedisValidateCodeManager {
	/**
	 * 过期秒数
 	 */
	private final static long REDIS_VALIDATE_CODE_TIMEOUT = 60 * 5;

	/**
	 * redis
	 */
	private final StringRedisTemplate stringRedisTemplate;

	@Autowired
	public RedisValidateCodeManager(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/**
	 * @Title isEmpty
	 * @Description 判断该键值对是否存在，若不存在，则新增，返回true，若存在，则返回false
	 * @param key  key
	 * @param value value
	 * @param time  消息过期时间(s)，若不设置，默认5分钟
	 * @return boolean
	 * @author H
	 * @date: 2020年4月23日
	 */
	@SuppressWarnings("unused")
	public Boolean isEmpty(String key, String value, Long time) {
		if (time != null) {
			return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
		}
		return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	@SuppressWarnings("unused")
	public void setValidateCode(String token, String code) {
		stringRedisTemplate.opsForValue().set(token, code, REDIS_VALIDATE_CODE_TIMEOUT, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unused")
	public void refreshValidateCode(String token, String code) {

		log.info("refreshValidateCode " + token + " " + code);

		stringRedisTemplate.opsForValue().set(token, code, REDIS_VALIDATE_CODE_TIMEOUT, TimeUnit.SECONDS);

		log.info("refreshValidateCode " + token + " " + code);

	}

	@SuppressWarnings("unused")
	public String getValidateCode(String token) {
		return stringRedisTemplate.opsForValue().get(token);
	}

	@SuppressWarnings("unused")
	public void delete(String token) {
		stringRedisTemplate.delete(token);
	}
}
