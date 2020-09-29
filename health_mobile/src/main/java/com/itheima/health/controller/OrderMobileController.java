package com.itheima.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 提交预约
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> submitMap){
//        获取redis
        Jedis jedis = jedisPool.getResource();
//        获取前端传入的手机号
        String telephone = submitMap.get("telephone");
//        验证码的key
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
//        验证码
        String codeInRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeInRedis)){
//           没值，重新发送
            return new Result(false,"请重新发送验证码" );
        }
//        前端传入的验证码
        String code = submitMap.get("validateCode");
        if (!codeInRedis.equals(code)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR );
        }
//        移除redis验证码，防止重复提交
//        jedis.del(key);
//      设置预约的类型
        submitMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submitOrder(submitMap);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order );
    }

    /**
     * 预约成功展示
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,Object> orderInfo = orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderInfo );
    }
}
