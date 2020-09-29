package com.itheima.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import com.itheima.health.utis.SMSUtils;
import com.itheima.health.utis.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 发送手机验证码
     * @param telephone
     * @return
     */
    @PostMapping("/send4Order")
    public Result send40rder(String telephone) {
//        获取redis
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
//        判断是否存在
        String codeInRedis = jedis.get(key);
        if (null == codeInRedis){
//            不存在
//            随机生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
//                发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone ,code+"" );
//                存入redis
                jedis.setex(key,15*60*60,code+"" );
//                发送成功，返回
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        return new Result(false,MessageConstant.SENT_VALIDATECODE );
    }
}
