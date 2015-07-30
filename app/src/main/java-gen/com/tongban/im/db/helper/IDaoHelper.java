package com.tongban.im.db.helper;

import java.util.List;

/**
 * 数据库操作接口
 *
 * @param <T> 数据类型
 */
public interface IDaoHelper<T extends Object> {
    public void addData(T bean);

    public void addAllData(Iterable<T> entities);

    public T getDataById(String id);

    public boolean hasKey(String id);

    public long getTotalCount();

    public void deleteData(String id);

    public List getAllData();

    public void deleteAll();
}