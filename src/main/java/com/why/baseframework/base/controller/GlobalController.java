package com.why.baseframework.base.controller;

import com.google.code.kaptcha.Producer;
import com.why.baseframework.base.web.response.ResponseResult;
import com.why.baseframework.base.web.response.ResponseUtils;
import com.why.baseframework.constants.IntConstants;
import com.why.baseframework.redis.RedisTransEncryptManager;
import com.why.baseframework.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenglin.wu
 * @description:
 * @title: GlobalController
 * @projectName tianrui
 * @date 2021年05月16日
 * @company WHY-Group
 */
@Slf4j
@RestController
@RequestMapping("/global")
public class GlobalController {
    @Autowired
    private RedisTransEncryptManager redisTransEncryptManager;
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    /**
     * 登录之前获取加密秘钥对密码进行加密处理
     *
     * @return ResponseResult<Map < String, String>>
     * @author W
     * @date 2021/5/16
     */
    @GetMapping("/encrypt")
    public ResponseResult<Map<String, String>> passwordEncrypt() {
        String transToken = TokenUtil.createToken();
        // 获取16位的随机字符串
        String transEncryptKey = RandomStringUtils.randomAlphabetic(IntConstants.INT_16);
        // 将随机字符串存到redis中
        this.redisTransEncryptManager.setTransEncryptKey(transToken, transEncryptKey);

        Map<String, String> map = new HashMap<String, String>();
        map.put("transToken", transToken);
        map.put("transEncryptKey", transEncryptKey);
        return ResponseUtils.success(map);
    }

    /**
     * 登录之前获取验证码的图片
     *
     * @return ResponseResult<Map < String, String>>
     * @author W
     * @date 2021/5/16
     */
    @GetMapping("/getVerificationCode")
    public ResponseResult<Map<String, String>> getVerificationCode() throws IOException {
        Map<String, String> map = new HashMap<>(IntConstants.INT_2);

        String text = captchaProducer.createText();
        log.info("验证码图片文字：{}",text);
        BufferedImage image = captchaProducer.createImage(text);
        FastByteArrayOutputStream outputStream = this.image2OutputStream(image);

        String transToken = TokenUtil.createToken();
        this.redisTransEncryptManager.setTransEncryptKey(transToken, text, IntConstants.INT_5);
        map.put("image", Base64Utils.encodeToString(outputStream.toByteArray()));
        map.put("transToken", transToken);

        return ResponseUtils.success(map);
    }

    /**
     * 将图片转换为输出流
     *
     * @return FastByteArrayOutputStream
     * @author W
     * @date 2021/5/16
     */
    private FastByteArrayOutputStream image2OutputStream(BufferedImage image) throws IOException {
        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        return outputStream;
    }
}
