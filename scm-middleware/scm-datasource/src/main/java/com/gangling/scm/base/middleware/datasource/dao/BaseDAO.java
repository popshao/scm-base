package com.gangling.scm.base.middleware.datasource.dao;

import com.gangling.scm.base.common.annotation.SFunction;
import com.gangling.scm.base.common.context.BaseContextHandler;
import com.gangling.scm.base.common.entity.BaseEntity;
import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.common.exception.ServerException;
import com.gangling.scm.base.common.page.BasePage;
import com.gangling.scm.base.common.page.BasePageParam;
import com.gangling.scm.base.common.page.PageResult;
import com.gangling.scm.base.middleware.datasource.mapper.BaseMapper;
import com.gangling.scm.base.middleware.datasource.model.WmsBaseEntity;
import com.gangling.scm.base.middleware.datasource.model.WmsWHBaseEntity;
import com.gangling.scm.base.utils.CollectionUtil;
import com.gangling.scm.base.utils.CommonUtil;
import com.gangling.scm.base.utils.ConvertUtils;
import com.gangling.scm.base.utils.DateUtil;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BaseDAO<M extends BaseMapper<T>, T extends BaseEntity> {

    @Autowired
    protected M mapper;

    @Autowired
    protected RedissonClient redissonClient;

    @Value("${spring.redis.key.prefix}")
    private String keyPrefix;

    protected String getEntityCamelName() {
        Class cls = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        String firstLettle = cls.getSimpleName().substring(0, 1).toLowerCase();
        String camelName = firstLettle + cls.getSimpleName().substring(1);
        return camelName;
    }

    private String getByIdCacheKeys(Long id) {
        return String.format("%s:%s:id_%d", keyPrefix, getEntityCamelName(), id);
    }

    public T getById(Object id) {
        if (id == null) {
            return null;
        }

        return mapper.selectByPrimaryKey(id);
/*        return RedisClient.get(getByIdCacheKeys(id), () -> {
            return mapper.selectByPrimaryKey(id);
        }, 10, TimeUnit.MINUTES);*/
    }

    public List<T> listByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }

        // 查询所有权限（超过1000个id，分批次查询）
        return CollectionUtil.grouped(idList, 1000)
                .parallelStream().map(list ->  mapper.selectByIds(CollectionUtil.LongListToString(list)))
                .flatMap(pList -> pList.stream()).collect(Collectors.toList());

//        String idListStr = CollectionUtil.LongListToString(idList);
//        return mapper.selectByIds(idListStr);
    }

    public List<T> listByKey(Object key, SFunction<T, ?> func) {
        return this.listByKey(key, func, null);
    }

    public List<T> listByKeyList(List<Object> keyList, SFunction<T, ?> func) {
        return this.listByKeyList(keyList, func, null);
    }

    public List<T> listByKey(Object key, SFunction<T, ?> func, Boolean isDelete) {
        if (CommonUtil.isEmpty(key)) {
            return null;
        }
        List<Object> keyList = new ArrayList<>(1);
        keyList.add(key);
        return this.listByKeyList(keyList, func, isDelete);
    }

    public List<T> listByKeyList(List<Object> keyList, SFunction<T, ?> func, Boolean isDelete) {
        if (CollectionUtils.isEmpty(keyList)) {
            return null;
        }
        if (CommonUtil.isEmpty(func)) {
            return null;
        }
        String columnName = CommonUtil.getAnnotationName(func);
        if (columnName == null) {
            return null;
        }
        // 查询所有权限（超过1000个id，分批次查询）
        return CollectionUtil.grouped(keyList, 1000)
                .parallelStream().map(list ->  mapper.listByKeyList(CollectionUtil.ObjListToString(list), columnName, isDelete != null ? (isDelete ? 1 : 0) : null))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    public T selectOne(T t) {
        return mapper.selectOne(t);
    }

    public List<T> selectList(T t) {
        return mapper.select(t);
    }

    public int deleteById(Long id) {
        if (id == null) {
            return 0;
        }

        int count = mapper.deleteByPrimaryKey(id);
//        if (count == 0) {
//            throw new BusinessException("删除失败!");
//        }
/*        if (count == 1) {
            RedisClient.deleteKey(getByIdCacheKeys(id));
        }*/
        return count;
    }

    public int deleteByIdList(List<Long> idList) {
//        String idListStr = CollectionUtil.LongListToString(idList);
//        int count = mapper.deleteByIds(idListStr);

        if (CollectionUtils.isEmpty(idList)) {
            return 0;
        }

        int count = CollectionUtil.grouped(idList, 1000)
                .parallelStream().map(list ->  mapper.deleteByIds(CollectionUtil.LongListToString(list))).reduce((i, j) -> i + j).orElse(0);

//        if (count == 0) {
//            throw new BusinessException("删除失败!");
//        }
/*        List<String> redisKeys = idList.stream()
                .filter(id -> id != null)
                .map(id -> getByIdCacheKeys(id))
                .collect(Collectors.toList());
        RedisClient.deleteKeys(redisKeys);*/
        return count;
    }

    public int save(T t) {
        int updateCount = 0;
        if (t.getId() != null) {
            setUpdateCommonColumn(t);
            updateCount = updateByPrimaryKeySelective(t);
        } else {
            setInsertCommonColumn(t);
            updateCount = mapper.insertSelective(t);
        }

/*        if (updateCount == 1) {
            RedisClient.deleteKey(getByIdCacheKeys(t.getId()));
        }*/

        return updateCount;
    }

    public int saveWithId(T t) {
        setInsertCommonColumn(t);
        return mapper.insertSelective(t);

/*        if (updateCount == 1) {
            RedisClient.deleteKey(getByIdCacheKeys(t.getId()));
        }*/
    }

    public int insertList(List<T> list) {
        if (CommonUtil.isEmpty(list)) {
            return 0;
        }
        list.forEach(this::setInsertCommonColumn);
        return CollectionUtil.grouped(list, 100)
                .parallelStream().map(subList ->  mapper.insertList(subList)).reduce((i, j) -> i + j).orElse(0);
    }

    @Deprecated
    public PageInfo<T> selectForPage(BasePage param, ISelect select) {
        return PageHelper.startPage(param.getPage(), param.getPageSize()).doSelectPageInfo(select);
    }

    public PageResult<T> selectForPage(BasePageParam param, ISelect select) {
        PageInfo<T> pageInfo = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(select);
        return PageResult.create(pageInfo.getList(), pageInfo.getTotal());
    }

    public PageResult<T> selectForPage(BasePageParam param, ISelect select, String orderBy) {
        PageInfo<T> pageInfo = PageHelper.startPage(param.getPageNo(), param.getPageSize(), orderBy).doSelectPageInfo(select);
        return PageResult.create(pageInfo.getList(), pageInfo.getTotal());
    }

    private int updateByPrimaryKeySelective(T t) {
        if (t instanceof WmsBaseEntity) {
            WmsBaseEntity updated = (WmsBaseEntity) t;
            if (updated.getVersion() != null) {
                int count = mapper.updateByPrimaryKeySelective(t);
                if (count == 0) {
                    throw new ServerException("更新失败!");
                }
                return count;
            }
        }
        return mapper.updateByPrimaryKeySelective(t);
    }

    private void setUpdateCommonColumn(T t) {
        if (t instanceof WmsBaseEntity) {
            WmsBaseEntity entity = (WmsBaseEntity) t;
            if (CommonUtil.isEmpty(entity.getUpdateBy())) {
                entity.setUpdateBy(BaseContextHandler.getLoginName());
            }
            entity.setUpdateTime(new Date());
        }
    }

    private void setInsertCommonColumn(T t) {
        if (t instanceof WmsWHBaseEntity) {
            WmsWHBaseEntity entity = (WmsWHBaseEntity) t;
            if (entity.getWarehouseId() == null) {
                throw new BusinessException("仓库ID不能为空");
            }
        }
        if (t instanceof WmsBaseEntity) {
            WmsBaseEntity entity = (WmsBaseEntity) t;
            if (CommonUtil.isEmpty(entity.getCreateBy())) {
                entity.setCreateBy(BaseContextHandler.getLoginName());
            }
            if (entity.getCreateTime() == null) {
                entity.setCreateTime(new Date());
            }
            entity.setVersion(0);
            if (CommonUtil.isEmpty(entity.getUpdateBy())) {
                entity.setUpdateBy(BaseContextHandler.getLoginName());
            }
            entity.setUpdateTime(new Date());
        }
    }

    public int logicalDeleteById(Long id) {
        if (id == null) {
            return 0;
        }
        T t = mapper.selectByPrimaryKey(id);
        if (ConvertUtils.toInt(CommonUtil.getFildByName("isDeleted", t), 1) == 1) {
            throw new BusinessException("删除失败!");
        }

        // 逻辑删除
        CommonUtil.setFildValueByName("isDeleted", 1, t);
        CommonUtil.setFildValueByName("updateBy", BaseContextHandler.getLoginName(), t);
        CommonUtil.setFildValueByName("updateTime", new Date(), t);
        int count = mapper.updateByPrimaryKeySelective(t);
        if (count == 0) {
            throw new BusinessException("删除失败!");
        }
/*        if (count == 1) {
            RedisClient.deleteKey(getByIdCacheKeys(id));
        }*/
        return count;
    }

    public int logicalDeleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return 0;
        }
        int count = CollectionUtil.grouped(idList, 1000)
                .parallelStream().map(list ->  mapper.logicalDeleteByIds(CollectionUtil.LongListToString(list), BaseContextHandler.getLoginName(), DateUtil.dateFormat(new Date(), DateUtil.DATETIME_PATTERN)))
                .reduce(Integer::sum).orElse(0);
        if (count == 0) {
            throw new BusinessException("删除失败!");
        }
/*        List<String> redisKeys = idList.stream()
                .filter(id -> id != null)
                .map(id -> getByIdCacheKeys(id))
                .collect(Collectors.toList());
        RedisClient.deleteKeys(redisKeys);*/
        return count;
    }

    public int updateByIds(T record, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return 0;
        }
        if (CommonUtil.isNotEmpty(record.getId())) {
            throw new BusinessException("updateByIds方法不能批量更新id");
        }
        int count = CollectionUtil.grouped(idList, 1000)
                .parallelStream().map(list -> mapper.updateByIds(record, CollectionUtil.LongListToString(list))).reduce(Integer::sum).orElse(0);

        if (count == 0) {
            throw new BusinessException("批量更新失败!");
        }
/*        List<String> redisKeys = idList.stream()
                .filter(id -> id != null)
                .map(id -> getByIdCacheKeys(id))
                .collect(Collectors.toList());
        RedisClient.deleteKeys(redisKeys);*/
        return count;
    }
}
