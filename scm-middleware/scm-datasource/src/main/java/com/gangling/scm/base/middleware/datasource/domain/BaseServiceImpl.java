package com.gangling.scm.base.middleware.datasource.domain;

import com.gangling.scm.base.common.annotation.SFunction;
import com.gangling.scm.base.middleware.datasource.dao.BaseDAO;
import com.gangling.scm.base.common.entity.BaseEntity;
import com.gangling.scm.base.middleware.datasource.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> implements BaseService<T> {

    @Autowired
    protected BaseDAO<M, T> dao;

    @Override
    public T getById(Long id) {
        return dao.getById(id);
    }

    @Override
    public List<T> listByIdList(List<Long> idList) {
        return dao.listByIdList(idList);
    }

    @Override
    public T selectOne(T t) {
        return dao.selectOne(t);
    }

    @Override
    public List<T> selectList(T t) {
        return dao.selectList(t);
    }

    @Override
    public int deleteById(Long id) {
        return dao.deleteById(id);
    }

    @Override
    public int deleteByIdList(List<Long> idList) {
        return dao.deleteByIdList(idList);
    }

    @Override
    public int save(T t) {
        return dao.save(t);
    }

    @Override
    public int insertList(List<T> list) {
        return dao.insertList(list);
    }

    @Override
    public int saveWithId(T t) {
        return dao.saveWithId(t);
    }

    @Override
    public List<T> listByKeyList(List<Object> keyList, SFunction<T, ?> func) {
        return dao.listByKeyList(keyList, func);
    }

    @Override
    public List<T> listByKeyList(List<Object> keyList, SFunction<T, ?> func, Boolean isDelete) {
        return dao.listByKeyList(keyList, func, isDelete);
    }

    @Override
    public List<T> listByKey(Object key, SFunction<T, ?> func) {
        return dao.listByKey(key, func);
    }

    @Override
    public List<T> listByKey(Object key, SFunction<T, ?> func, Boolean isDelete) {
        return dao.listByKey(key, func, isDelete);
    }
}
