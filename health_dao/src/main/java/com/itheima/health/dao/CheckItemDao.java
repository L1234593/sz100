package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    /**
     * 查询所有检查项
     */

    List<CheckItem> findAll();

    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

    CheckItem findById(int id);

    void update(CheckItem checkItem);

    void deleteById(int id);

    int findCountByCheckItemId(int id);
}
