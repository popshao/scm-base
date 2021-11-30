package com.gangling.scm.base.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static <T> List<List<T>> grouped(List<T> list, int grouped) {
        if (null == list) {
            return Collections.emptyList();
        }
        if (grouped <= 0 || list.isEmpty()) {
            return Collections.singletonList(list);
        }
        int size = list.size();

        List<List<T>> result = new ArrayList<>(size / grouped + 1);

        int fromIndex = 0;
        int toIndex = grouped;

        while (fromIndex < size) {
            if (toIndex > size) {
                toIndex = size;
            }
            if (fromIndex < toIndex) {
                result.add(list.subList(fromIndex, toIndex));
            }

            fromIndex = toIndex;
            toIndex += grouped;
        }

        return result;
    }

    public static String LongListToString(List<Long> idList) {
        return idList.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public static String ObjListToString(List<Object> objects) {
        return objects.stream().map(o -> "'" + o + "'").collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        List<Object> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 34, 2, 3, 4, 5, 6, 2, 1, 12, 3, 4, 5, 6, 5, 7, 8, 23, 43, 545, 4, 56, 565, 65, 6);
       // List<List<Integer>> grouped = grouped(list, 11);
        //grouped.forEach(group -> System.out.println(Arrays.toString(group.toArray())));
        System.out.println(ObjListToString(list));
    }
}
