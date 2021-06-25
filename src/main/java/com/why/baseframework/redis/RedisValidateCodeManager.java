package com.why.baseframework.redis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisValidateCodeManager {

	private final static long Redis_Validate_Code_Timeout = 60 * 5; // 过期秒数

	private static Logger logger = LoggerFactory.getLogger(RedisValidateCodeManager.class);

	@Autowired
	private StringRedisTemplate stringRedisTemplate;// redis

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
	public boolean isEmpty(String key, String value, Long time) {
		if (time != null) {
			return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
		}
		return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	public void setValidateCode(String token, String code) {
		stringRedisTemplate.opsForValue().set(token, code, Redis_Validate_Code_Timeout, TimeUnit.SECONDS);
	}

	public void refreshValidateCode(String token, String code) {

		logger.info("refreshValidateCode " + token + " " + code);

		stringRedisTemplate.opsForValue().set(token, code, Redis_Validate_Code_Timeout, TimeUnit.SECONDS);

		logger.info("refreshValidateCode " + token + " " + code);

	}

	public String getValidateCode(String token) {
		return stringRedisTemplate.opsForValue().get(token);
	}

	public void delete(String token) {
		stringRedisTemplate.delete(token);
	}
}
