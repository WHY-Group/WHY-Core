package com.why.baseframework.redis;

import com.why.baseframework.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @Author Y
 * @Description: redis中对user的常规操作，如果其他端的用户需要使用,请以Redis（）LoginUserManager格式创建类
 * @Title: RedisLoginUserManager
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
@Component
@Slf4j
public class RedisLoginUserManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加用户到redis中
     *
     * @param loginUser
     * @Author Y
     * @Description
     * @Date 2021/4/16 16:17
     **/
    public void setLoginUser(LoginUser loginUser) {
        Assert.notNull(loginUser.getRedisId(), "token为userId，设置Redis用户失败");
        Assert.notNull(loginUser.getToken(), "token为null，设置Redis用户失败");

        // 检查redis中是否存储当前用户，如果有则删除，并重新添加
        LoginUser oldLoginUser = this.getLoginUserByToken(loginUser.getRedisId());
        if (oldLoginUser != null) {
            this.deleteLoginUser(oldLoginUser.getToken());
        }
        log.info("RedisLoginUserManager createLoginUser login name:{} , login token:{} , login id:{}", loginUser.getName(), loginUser.getToken(), loginUser.getRedisId());

        // 添加token
        stringRedisTemplate.opsForValue().set(loginUser.getToken(), loginUser.getRedisId(),
                RedisTimeOutConstants.REDIS_LOGIN_USER_TIMEOUT, TimeUnit.SECONDS);
        // 添加登录对象
        redisTemplate.opsForValue().set(loginUser.getRedisId(), loginUser, RedisTimeOutConstants.REDIS_LOGIN_USER_TIMEOUT,
                TimeUnit.SECONDS);
    }

    /**
     * 获取redis中的LoginUser
     *
     * @param token
     * @Return {@link LoginUser }
     * @Author Y
     * @Date 2021/4/16 16:17
     **/
    public LoginUser getLoginUserByToken(String token) {
        // 判断token是否为空，为空则返回null
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String loginUserId = getLoginUserId(token);
        // 判断登录对象是否为空，为空则返回null
        if (StringUtils.isBlank(loginUserId)) {
            return null;
        }
        return this.getLoginUserByUserId(loginUserId);
    }

    /**
     * 删除redis中的user
     *
     * @param token
     * @Author Y
     * @Date 2021/4/16 16:17
     **/
    public void deleteLoginUser(String token) {
        LoginUser loginUser = this.getLoginUserByToken(token);
        if (loginUser != null) {
            redisTemplate.delete(loginUser.getRedisId());
        }
        stringRedisTemplate.delete(token);
    }

    /**
     * 更新redis中的LoginUser
     *
     * @param loginUser
     * @Author Y
     * @Date 2021/4/16 16:18
     **/
    public void delayLoginUserTime(LoginUser loginUser) {

        // 过期时间
        Long expire = stringRedisTemplate.getExpire(loginUser.getToken(), TimeUnit.SECONDS);

        // 需要更新expire
        if (expire == null || expire < RedisTimeOutConstants.REDIS_LOGIN_USER_EXPIRE) {
            log.info("RedisLoginUserManager delayLoginUserTime login name:{} , login token:{} , login id:{}", loginUser.getName(), loginUser.getToken(), loginUser.getRedisId());
            stringRedisTemplate.expire(loginUser.getToken(), RedisTimeOutConstants.REDIS_LOGIN_USER_TIMEOUT, TimeUnit.SECONDS);
            redisTemplate.expire(loginUser.getRedisId(), RedisTimeOutConstants.REDIS_LOGIN_USER_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    /**
     * 通过userId获取redis中的LoginUser
     *
     * @param userId
     * @Return {@link LoginUser }
     * @Author Y
     * @Date 2021/4/16 16:18
     **/
    public LoginUser getLoginUserByUserId(String userId) {
        if (redisTemplate == null) {
            log.error("RedisLoginUserManager getLoginUserByUserId stringRedisTemplate is null");
        } else {
            Object obj = redisTemplate.opsForValue().get(userId);
            if (obj != null) {
                return (LoginUser) obj;
            }
        }
        return null;
    }

    /**
     * 从redis获取userId
     *
     * @param token
     * @Return {@link String }
     * @Author Y
     * @Date 2021/4/16 14:23
     **/
    public String getLoginUserId(String token) {
        log.info("RedisLoginUserManager getLoginUserId token" + token);
        if (stringRedisTemplate == null) {
            log.error("RedisLoginUserManager getLoginUserId stringRedisTemplate is null");
        } else {
            Object obj = stringRedisTemplate.opsForValue().get(token);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }

    public void deleteLoginUserById(String... redisId){
        log.info("RedisLoginUserManager deleteLoginUserById id:{}", redisId);
        if (ObjectUtils.anyNull(redisTemplate)){
            log.error("RedisLoginUserManager deleteLoginUserById redisTemplate is null");
        }else {
            for (String id : redisId) {
                Object obj = redisTemplate.opsForValue().get(id);
                if (ObjectUtils.allNotNull(obj) && obj instanceof LoginUser){
                    this.deleteLoginUser(((LoginUser) obj).getToken());
                }
            }
        }
    }

}
