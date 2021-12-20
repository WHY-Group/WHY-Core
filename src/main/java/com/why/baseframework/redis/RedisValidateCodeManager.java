package com.why.baseframework.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class RedisValidateCodeManager {
	// 过期秒数
	private final static long REDIS_VALIDATE_CODE_TIMEOUT = 60 * 5;

	// redis
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * @Title isEmpty
	 * @Description 判断该键值对是否存在，若不存在，则新增，返回true，若存在，则返回false
	 * @param key
	 * @param value
	 * @param time  消息过期时间(s)，若不设置，默认5分钟
	 * @return
	 * @return boolean
	 * @author 胡斌
	 * @date: 2020年4月23日
	 */
	public Boolean isEmpty(String key, String value, Long time) {
		if (time != null) {
			return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
		}
		return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	public void setValidateCode(String token, String code) {
		stringRedisTemplate.opsForValue().set(token, code, REDIS_VALIDATE_CODE_TIMEOUT, TimeUnit.SECONDS);
	}

	public void refreshValidateCode(String token, String code) {

		log.info("refreshValidateCode " + token + " " + code);

		stringRedisTemplate.opsForValue().set(token, code, REDIS_VALIDATE_CODE_TIMEOUT, TimeUnit.SECONDS);

		log.info("refreshValidateCode " + token + " " + code);

	}

	public String getValidateCode(String token) {
		return stringRedisTemplate.opsForValue().get(token);
	}

	public void delete(String token) {
		stringRedisTemplate.delete(token);
	}
}
