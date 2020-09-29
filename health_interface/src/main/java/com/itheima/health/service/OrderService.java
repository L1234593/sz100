package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;

import java.util.Map;

public interface OrderService {
    /**
     * 提交预约
     * @param submitMap
     * @return
     */
    Order submitOrder(Map<String, String> submitMap) throws HealthException;

    /**
     * 预约成功展示
     * @param id
     * @return
     */
    Map<String,Object> findById(int id);
}
