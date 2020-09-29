package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utis.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时清理七牛上的垃圾图片
 */
@Component("cleanImgJob")
public class CleanImgJob {
    /**
     * 订阅服务
     */
    @Reference
    private SetmealService setmealService;

    public void cleanImg(){
//        查出七牛上的图片
        List<String> qiNiuImgs = QiNiuUtils.listFile();
//        查出数据库的图片
        List<String> mySQLImgs = setmealService.findImgs();
//        七牛减去数据库图片
        qiNiuImgs.removeAll(mySQLImgs);
//        把剩下的图片转成数组
        String[] strings = qiNiuImgs.toArray(new String[]{});
//        删除七牛上的图片
        QiNiuUtils.removeFiles(strings);
    }
}
