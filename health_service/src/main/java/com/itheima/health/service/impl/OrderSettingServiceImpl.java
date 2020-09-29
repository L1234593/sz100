package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettingList) {
//        遍历
        for (OrderSetting orderSetting : orderSettingList) {
//            判断是否存在，通过日期来查询，注意：日期里是否有时分秒，数据库里的日期是没有时分秒的
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if (null != osInDB){
//                数据库存在这个预约设置，已预约数量不能大于可预约数量
                if (osInDB.getReservations() > orderSetting.getNumber()){
                    throw new HealthException(orderSetting.getOrderDate()+"中已预约数量不能大于可预约数量");
                }
                orderSettingDao.updateNumber(orderSetting);
            }else {
//                不存在
                orderSettingDao.add(orderSetting);
            }
        }
    }

//    根据日期查询预约设置数据
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        //1.组织查询map，dateBegin表示月份开始时间，dateEnd月份结束时间
        String startDate = month + "-01";//2019-03-01
        String endDate = month + "-31";//2019-03-31
        List<Map<String,Integer>> monthData = orderSettingDao.getOrderSettingByMonth(startDate,endDate);
        return monthData;
    }


    /**
     * 通过日期设置预约信息
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) throws HealthException {
// 通过日期查询预约设置信息
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        // 存在
        if(null != osInDB) {
            //    是否 要设置的最大预约数量 < 已预约数量
            if(orderSetting.getNumber() < osInDB.getReservations()) {
                //       是 抛出异常
                throw new HealthException("最大预约数量不能小于已预约数量");
            }
            //       否，通过日期更新最大预约数
            orderSettingDao.updateNumber(orderSetting);
        }else {
            // 不存在
            //   插入预约设置
            orderSettingDao.add(orderSetting);
        }
    }
}
