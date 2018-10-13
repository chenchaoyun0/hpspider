package com.megvii.dzh.spider.service;

import java.util.List;

public interface IBaseService<T>{
    int deleteByPrimaryKey(String id);

    int insert(T t);

    void insertBatch(List<T> list);
    
    int insertSelective(T t);

    T selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);
    
    List<T> selectList(T t);

    long count(T t);

    public int getLastId();

}
