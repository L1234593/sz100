package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utis.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //-获取原有图片名称，截取到后缀名
        String originalFilename = imgFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //-生成唯一文件名，拼接后缀名
        String filename = UUID.randomUUID()+ extension;
        //-调用七牛云上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),filename );
            //-返回数据给页面
            Map<String,String> map = new HashMap<>();
            map.put("imgName", filename);
            map.put("domain",QiNiuUtils.DOMAIN );
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_FAIL );
    }

    /**
     * 添加套餐信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.add(setmeal,checkgroupIds);
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS );
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务分页查询
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant. QUERY_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    @PostMapping("/findById")
    public Result findById(int id){
//        调用服务查询
        Setmeal setmeal = setmealService.findById(id);
//        前端要显示图片需要全路径
//        封装到map中，解决图片路径问题
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("setmeal",setmeal );//formData
        resultMap.put("domain",QiNiuUtils.DOMAIN );//domain
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap );
    }

    /**
     * 通过id查询选中的检查组ids
     * @param id
     * @return
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
         List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
         return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds );
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
//        调用业务服务修改
        setmealService.update(setmeal,checkgroupIds);
//        响应结果
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS );
    }

    /**
     * 删除套餐
     */
    @PostMapping("/deleteById")
    public Result delete(Integer id){
        setmealService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
