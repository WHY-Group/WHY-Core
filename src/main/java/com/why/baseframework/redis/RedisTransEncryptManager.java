package com.why.baseframework.redis;

import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.constants.IntConstants;
import com.why.baseframework.enums.ErrCodeEnum;
import com.why.baseframework.util.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author Y
 * @Description: redis中各个过期时间配置
 * @Title: RedisTimeOutConstants
 * @ProjectName base_framework
 * @Date 2021/4/16
 * @Company WHY-Group
 **/
@Component
public class RedisTransEncryptManager {

    /**
     * redis操作
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 将传输加密存到redis中
     *
     * @param transToken 传输token
     * @param encryptKey 秘钥
     * @return void
     * @author W
     * @date 2021/5/16
     */
    public void setTransEncryptKey(String transToken, String encryptKey) {
        stringRedisTemplate.opsForValue().set(transToken, encryptKey, IntConstants.INT_10,
                TimeUnit.MINUTES);
    }

    /**
     * 将传输加密存到redis中
     *
     * @param transToken 传输token
     * @param encryptKey 秘钥
     * @param minute     过期时间多少分钟
     * @return void
     * @author W
     * @date 2021/5/16
     */
    public void setTransEncryptKey(String transToken, String encryptKey, int minute) {
        stringRedisTemplate.opsForValue().set(transToken, encryptKey, IntConstants.INT_10,
                TimeUnit.MINUTES);
    }

    /**
     * 传输Token仅使用一次,获取并删除传输Token
     *
     * @param transToken 传输的加密key
     * @return String
     * @author W
     * @date 2021/5/16
     */
    public String getAndDelTransEncryptToken(String transToken) {
        String redisEncryptKey = stringRedisTemplate.opsForValue().get(transToken);
        if (StringUtils.isNotBlank(redisEncryptKey)) {
            stringRedisTemplate.delete(transToken);
        }
        return redisEncryptKey;
    }

    /**
     * 通过transToken加密
     *
     * @return String
     * @param: transToken the transToken
     * @param: encryptStr 加密字符串
     * @author W
     * @date 2021-05-29
     */
    public String getAndDelTransTokenAndDecoder(String transToken, String encryptStr) throws Exception {
        if (StringUtils.isBlank(encryptStr)) {
            throw new BusinessException(ErrCodeEnum.DATA_CHECK.getCode(), "请传入加密字符串");
        }
        String redisEncryptKey = stringRedisTemplate.opsForValue().get(transToken);
        if (StringUtils.isNotBlank(redisEncryptKey)) {
            stringRedisTemplate.delete(transToken);
        }
        return EncryptUtils.aesDecrypt(encryptStr, redisEncryptKey);
    }

}
