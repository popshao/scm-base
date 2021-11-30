package com.gangling.scm.base.middleware.datasource.domain;

import com.gangling.scm.base.common.annotation.SFunction;
import com.gangling.scm.base.common.entity.BaseEntity;

import java.util.List;

public interface BaseService<T extends BaseEntity> {
    T getById(Long id);

    List<T> listByIdList(List<Long> idList);

    T selectOne(T t);

    List<T> selectList(T t);

    int deleteById(Long id);

    int deleteByIdList(List<Long> idList);

    int save(T t);

    int insertList(List<T> list);

    int saveWithId(T t);

    List<T> listByKeyList(List<Object> keyList, SFunction<T, ?> func);

    List<T> listByKeyList(List<Object> keyList, SFunction<T, ?> func, Boolean isDelete);

    List<T> listByKey(Object key, SFunction<T, ?> func);

    List<T> listByKey(Object key, SFunction<T, ?> func, Boolean isDelete);
}
