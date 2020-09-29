package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    /**
     * 批量导入
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    List<Map<String, Integer>> getOrderSettingByMonth(String month);
    // 通过日期修改可预约人数，这里要抛出自定义的异常
    void editNumberByDate(OrderSetting orderSetting)throws HealthException;
}
