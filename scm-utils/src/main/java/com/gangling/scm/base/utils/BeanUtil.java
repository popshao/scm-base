package com.gangling.scm.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.common.page.PageResult;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对象之间转换工具类
 */
@Slf4j
public class BeanUtil {


    public static <T> T deepCopy(T obj) {
        if (Objects.isNull(obj))
            return null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("对象深拷贝异常", e);
            throw new BusinessException("对象深拷贝异常");
        }
    }


    public static <T, R> R deepCopyWithJSON(T obj, Class<R> rClass) {
        if (Objects.isNull(obj))
            return null;
        //禁用 fastJSON 循环引用检测特性
        String jsonObject = JSONObject.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
        return JSON.parseObject(jsonObject, rClass);
    }

    /**
     * 对象复制
     * 支持 深度复制
     * 不支持静态内部类的复制
     *
     * @param source
     * @param target
     * @param <F>
     * @param <R>
     * @return
     */
    @Deprecated
    public static <F, R> R copy(F source, Class<R> target) {
        try {
            R r = target.newInstance();
            BeanUtils.copyProperties(r, source);
            return r;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 对象复制，采用spring的方法，速度更快
     *
     * @param source
     * @param targetClass
     * @param <F>
     * @param <R>
     * @return
     */
    public static <F, R> R copySpring(F source, Class<R> targetClass) {
        try {
            if (source == null) return null;
            R r = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, r);
            return r;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 列表复制
     *
     * @param col 原始列表
     * @param c   转化对象
     * @param <F>
     * @param <R>
     * @return 转化对象列表
     */
    public static <F, R> List<R> copyList(Collection<F> col, Class<R> c) {
        return CollectionUtils.isEmpty(col) ? Lists.newArrayList() : col.stream().map(s -> copySpring(s, c)).collect(Collectors.toList());
    }

    /**
     * 对象复制，采用modelMapper的实现，据说比spring更快，还能深拷贝
     *
     * @param source
     * @param target
     * @param <F>
     * @param <R>
     * @return
     */
//    public static <F, R> R copyModel(F source, Class<R> target) {
//        try {
//            ModelMapper modelMapper = new ModelMapper();
//            R r = modelMapper.map( source, target );
//            return r;
//        } catch (Exception e) {
//            throw new BusinessException(e);
//        }
//    }
    /**
     * 判空对象复制 ，采用modelMapper的实现，据说比spring更快，还能深拷贝
     *
     * @param source
     * @param target
     * @param <F>
     * @param <R>
     * @return
     */
//    public static <F, R> R copyModelWithCheck(F source, Class<R> target) {
//        return Objects.nonNull(source) ? copyModel(source,target) : null;
//    }

    /**
     * 忽略部分属性的拷贝
     *
     * @param source
     * @param target
     * @param ignoreProperties
     * @param <F>
     * @param <R>
     * @return
     */
    public static <F, R> R copyIgnore(F source, Class<R> target, String... ignoreProperties) {
        try {
            R r = target.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, r, ignoreProperties);
            return r;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 消除空判断的三段表达式
     *
     * @param obj           箱要取的值
     * @param defaultReturn 值为空时的默认值
     * @param <T>
     * @return
     */
    public static <T> T getOrDefault(T obj, T defaultReturn) {
        return getDefaultWithEqual(obj, null, defaultReturn);
    }

    /**
     * 对象获取默认属性方法
     * <p>
     * 代替类型：bean == null || bean.getId() == null ? 0 : bean.getId();
     *
     * @param obj           对象
     * @param getFun        属性的get方法
     * @param defaultReturn 默认值
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R getOrDefault(T obj, Function<T, R> getFun, R defaultReturn) {

        if (obj == null) {
            return defaultReturn;
        }

        if (getFun.apply(obj) == null) {
            return defaultReturn;
        }

        return getFun.apply(obj);
    }

    /**
     * 如果需求值和期望值相等时，返回默认值
     *
     * @param obj           需求值
     * @param equalVal      期望值
     * @param defaultReturn 默认值
     * @param <T>
     * @return
     */
    public static <T> T getDefaultWithEqual(T obj, T equalVal, T defaultReturn) {
        if (equalVal == null && obj == null) {
            return defaultReturn;
        }

        if (Objects.equals(equalVal, obj)) {
            return defaultReturn;
        }
        return obj;
    }

    public static <F, R> PageInfo<R> copyPage(PageInfo<F> source, Class<R> targetClass) {
        try {
            if (source == null) {
                return null;
            } else {
                List<R> rList = BeanUtil.copyList(source.getList(), targetClass);
                PageInfo<R> r = new PageInfo<>(rList);
                r.setPageNum(source.getPageNum());
                r.setPageSize(source.getPageSize());
                r.setTotal(source.getTotal());
                return r;
            }
        } catch (Exception var3) {
            throw new BusinessException(var3);
        }
    }

    public static <F, R> PageResult<R> copyPage(PageResult<F> source, Class<R> targetClass) {
        try {
            if (source == null) {
                return null;
            } else {
                List<R> rList = BeanUtil.copyList(source.getResult(), targetClass);
                return PageResult.create(rList, source.getTotal());
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

}
