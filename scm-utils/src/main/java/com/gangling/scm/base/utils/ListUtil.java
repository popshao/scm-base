package com.gangling.scm.base.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * list相关处理的工具类
 * <p>
 * ListUtil.java
 * </p>
 *
 * @author wxy
 * @version 1.0 2009-3-19
 */
public class ListUtil {
    private static Log log = LogFactory.getLog(ListUtil.class);

    /**
     * List是否为空的验证
     *
     * @param parm
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNullOrEmpty(List parm) {
        boolean rtn = false;
        if (null == parm || parm.size() == 0) {
            rtn = true;
        }
        return rtn;
    }

    /**
     * List是否不为空的验证
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(List parm) {
        return !isNullOrEmpty(parm);
    }

    /**
     * 查找objList不在baseList中的数据
     *
     * @param <T>
     * @param baseList
     * @param objList
     * @return List<T> 返回等于objList的数据或者非空的List
     * @author wxy
     */
    public static <T> List<T> findNotIncludeValues(List<T> baseList, List<T> objList) {
        if (isNullOrEmpty(baseList)) {
            return objList;
        }
        if (isNullOrEmpty(objList)) {
            return new ArrayList<T>(0);
        }
        List<T> r = new ArrayList<T>(objList.size());
        HashMap<T, String> map = new HashMap<T, String>(baseList.size());
        for (T o : baseList) {
            map.put(o, "1");
        }
        for (T o : objList) {
            if (map.get(o) == null) {
                r.add(o);
            }
        }
        return r;
    }

    /**
     * 查找objList不在baseList中的数据
     *
     * @param <T>
     * @param baseList
     * @param objList
     * @param idFieldName 对象的主键fieldName
     * @return List<T> 返回等于objList的数据或者非空的List
     * @author wxy
     */
    public static <T> List<T> findNotIncludeValues(List<T> baseList, List<T> objList,
                                                   String idFieldName) {
        if (isNullOrEmpty(baseList)) {
            return objList;
        }
        if (isNullOrEmpty(objList)) {
            return new ArrayList<T>(0);
        }
        if (StringUtil.isEmpty(idFieldName)) {
            return findNotIncludeValues(baseList, objList);
        }
        List<T> r = new ArrayList<T>(objList.size());
        try {
            Method idMethod = baseList.get(0).getClass()
                    .getMethod("get" + StringUtil.upperCaseFirstCharacter(idFieldName));

            HashMap<Object, String> map = new HashMap<Object, String>(baseList.size());
            for (T o : baseList) {
                map.put(idMethod.invoke(o), "1");
            }
            for (T o : objList) {
                if (map.get(idMethod.invoke(o)) == null) {
                    r.add(o);
                }
            }
        } catch (Exception e) {
            log.error("error,idFieldName is:" + idFieldName, e);
        }
        return r;
    }

    /**
     * 查询o在dataList中的位置.从0开始
     *
     * @param dataList    数据列表的list
     * @param o           目标对象
     * @param idFieldName 主码的字段名称 如果为空,则直接使用对象引用进行比较
     * @return int >=0：位置 -1:未找到
     */
    public static int getPosFromList(List dataList, Object o, String idFieldName) {
        int r = -1;
        if (dataList == null || dataList.isEmpty() || o == null) {
            return r;
        }
        if (idFieldName == null) {// 如果没有指定主码,则自动比较两个对象的引用
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i) == o) {
                    r = i;
                    break;
                }
            }
        } else {
            try {
                Method idMethod = o.getClass().getMethod(
                        "get" + StringUtil.upperCaseFirstCharacter(idFieldName));
                Object key = idMethod.invoke(o, null);
                Object data = null;
                for (int i = 0; i < dataList.size(); i++) {
                    data = dataList.get(i);
                    if (data != null) {
                        if (key.equals(idMethod.invoke(data, null))) {
                            r = i;
                        }
                    }
                }

            } catch (Exception e) {
                // 在方法名称不对的时候,系统在后台进行提示
                log.error("May idFieldName is wrong! idFieldName:" + idFieldName + ",className:"
                        + o.getClass().getName());
                e.printStackTrace();
            }
        }
        return r;
    }

    /**
     * 查询o在dataList中的位置.从0开始
     *
     * @param dataList    数据列表的list
     * @param idValue     idValue 目标对象的主键
     * @param idFieldName 主码的字段名称 如果为空,则直接使用对象引用进行比较
     * @return int >=0：位置 -1:未找到
     */
    public static int getPosFromListIdValue(List dataList, Object idValue, String idFieldName) {
        int r = -1;
        if (dataList == null || dataList.isEmpty() || idValue == null) {
            return r;
        }
        try {
            Method idMethod = dataList.get(0).getClass()
                    .getMethod("get" + StringUtil.upperCaseFirstCharacter(idFieldName));
            Object value = null;
            int pos = 0;
            for (Object obj : dataList) {
                value = idMethod.invoke(obj, null);
                if (value.toString().equals(idValue.toString())) {
                    r = pos;
                    break;
                }
                pos++;
            }
        } catch (Exception e) {
            // 在方法名称不对的时候,系统在后台进行提示
            log.error("May idFieldName is wrong! idFieldName:" + idFieldName);
        }
        return r;
    }

    /**
     * 排除list中重复的记录
     *
     * @param list
     * @return
     */
    public static <T> List<T> uniqueList(List<T> list) {
        return list == null ? null : ImmutableSet.copyOf(list).asList();
    }

    /**
     * 排除list中重复的记录
     *
     * @param list
     * @return
     */
    public static List uniqueList(List list, String idFieldName) {
        if (isNullOrEmpty(list)) {
            return list;
        }
        if (StringUtil.isEmpty(idFieldName)) {
            return uniqueList(list);
        }
        List r = list;
        try {
            Method idMethod = list.get(0).getClass()
                    .getMethod("get" + StringUtil.upperCaseFirstCharacter(idFieldName));
            Object value = null;
            Map map = new HashMap(list.size());
            for (Object o : list) {
                map.put(idMethod.invoke(o, null), "1");
            }
            if (map.size() != list.size()) {
                r = new ArrayList(map.size());
                for (Object o : list) {
                    value = idMethod.invoke(o, null);
                    if (map.get(value) != null) {
                        r.add(o);
                        map.remove(value);
                    }
                }
            }
            map = null;
            list = null;
        } catch (Exception e) {
            // 在方法名称不对的时候,系统在后台进行提示
            log.error("May idFieldName is wrong! idFieldName:" + idFieldName);
            e.printStackTrace();
        }
        return r;

    }

    /**
     * 将List等量拆分成多个List
     * @param list
     * @param splitSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list))
            return Collections.emptyList();
        //计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 合并列表中的元素
     * </pre>
     *
     * @param <T>
     * @param list
     * @param listMegareOpr
     * @return
     */
    public static <T> void megareList(List<T> list, ListMegareOpr<T> listMegareOpr) {
        for (int i = 0; i < list.size(); i++) {
            T tMain = list.get(i);
            for (int j = 1; j < list.size(); j++) {
                T tTemp = list.get(j);
                if (i == j)
                    continue;

                if (listMegareOpr.isNeedMegare(tMain, tTemp)) {
                    listMegareOpr.megareOpr(tMain, tTemp);
                    list.remove(tTemp);
                    j--;
                }
            }
        }
    }

    public static String collection2String(Collection<String> list, String seprater) {
        String result = "";
        if (list == null || list.isEmpty()) {
            result = "";
        } else {
            StringBuffer sb = new StringBuffer();
            Iterator<String> i = list.iterator();
            for (boolean first = true; i.hasNext(); first = false) {
                if (!first) {
                    sb.append(seprater);
                }
                sb.append(i.next());
            }
            result = sb.toString();
        }
        return result;
    }

    public static <T> void add2Map(Map<String, List<T>> map, String key, T value) {
        List<T> list = map.get(key);
        if (list == null) {
            list = new ArrayList<T>();
            map.put(key, list);
        }
        list.add(value);
    }

    public static <T> boolean isExists(List<T> list, T obj, String property) {
        if (list != null && obj != null) {
            try {
                Method idMethod = obj.getClass().getMethod(
                        "get" + StringUtil.upperCaseFirstCharacter(property));
                Object key = idMethod.invoke(obj, null);
                Object data = null;
                for (int i = 0; i < list.size(); i++) {
                    data = list.get(i);
                    if (data != null) {
                        if (key.equals(idMethod.invoke(data, null))) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static <T> List<T> subList(List<T> lists, int endIndex) {
        return subList(lists, 0, endIndex);
    }

    public static <T> List<T> subList(List<T> lists, int startIndex, int endIndex) {
        if (ListUtil.isNotEmpty(lists)) {
            if (endIndex <= 0) {
                return Lists.newArrayList();
            } else if (endIndex > lists.size()) {
                endIndex = lists.size();
            }

            if (startIndex < 0) {
                startIndex = 0;
            }

            if (startIndex >= endIndex) {
                return Lists.newArrayList();
            }

            return lists.subList(startIndex, endIndex);
        }
        return Lists.newArrayList();
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public static interface ListMegareOpr<T> {
        public boolean isNeedMegare(T t1, T t2);

        public void megareOpr(T t1, T t2);
    }



    /**
     * 遍历处理list
     */
    public static void ergodic(List list, Function<List , Object> function, int pagesize){
        if(ListUtil.isNotEmpty(list)){
            //分页 遍历 处理list获取
            int size = list.size();
            int pageTotal = size % pagesize == 0 ? (size / pagesize) : (size / pagesize + 1);
            for (int page = 0; page < pageTotal; page++) {
                int start = page * pagesize;
                int end = (page + 1) * pagesize;
                end = end > size ? size : end;
                List  sublist =  list.subList(start, end);
                function.apply(sublist);
            }
        }
    }

    /**
     * 集合拆分 -> 处理 -> 合并
     */
    public static <E, T> List<T> ergodicRt(List<E> list, Function<List<E> , List<T>> function, int pagesize){
        List<T> returnList = new ArrayList<>();
        if (ListUtil.isNullOrEmpty(list)) {
            return returnList;
        }
        //分页 遍历 处理list获取
        int size = list.size();
        int pageTotal = size % pagesize == 0 ? (size / pagesize) : (size / pagesize + 1);
        for (int page = 0; page < pageTotal; page++) {
            int start = page * pagesize;
            int end = (page + 1) * pagesize;
            end = Math.min(end, size);
            List<E> sublist = list.subList(start, end);
            returnList.addAll(function.apply(sublist));
        }
        return returnList;
    }

    public static List<String> splitStrToList(String str) {
        return splitStr(str, ",", String::toString);
    }

    public static List<Long> splitStrToLongList(String str) {
        return splitStr(str, ",", Long::parseLong);
    }

    public static <T> List<T> splitStr(String str, String delimiter, Function<String, T> func) {
        if (str == null) {
            return new ArrayList<>();
        }
        if (delimiter == null) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        String[] arr = str.split(delimiter);
        for (String s : arr) {
            list.add(func.apply(s));
        }
        return list;
    }

    /**
     * 提取集合中的某一列, 并去null元素
     */
    public static <T, R> List<R> extractColumn(List<T> list, Function<T, R> func) {
        if (isNullOrEmpty(list)) {
            return new ArrayList<>();
        }
        List<R> returnList = list.stream().map(func).distinct().collect(Collectors.toList());
        returnList.removeAll(CommonUtil.nullCollection());
        return returnList;
    }

    /**
     * List转Map
     */
    public static <K, T> Map<K, T> listToMap(List<T> list, Function<T, K> keyFunc) {
        return listToMap(list, keyFunc, Function.identity());
    }

    public static <K, V, T> Map<K, V> listToMap(List<T> list, Function<T, K> keyFunc, Function<T, V> valuFunc) {
        if (isNullOrEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(keyFunc, valuFunc, (v1, v2) -> v1));
    }

    /**
     * 将List分割成指定个数的集合
     * @param list
     * @param countList
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitListWithCountList(List<T> list, Integer countList) {
        Map<Integer, List<T>> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            int listIndex = i % countList;
            List<T> tempList = map.get(listIndex);
            if (tempList == null) {
                tempList = new ArrayList<>();
            }
            tempList.add(list.get(i));
            map.put(listIndex, tempList);
        }

        List<List<T>> returnList = new ArrayList<>();
        returnList.addAll(map.values());
        return returnList;
    }
}
