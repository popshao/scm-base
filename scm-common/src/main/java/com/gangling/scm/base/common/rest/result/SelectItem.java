package com.gangling.scm.base.common.rest.result;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhukai
 * @since 2021/10/25
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SelectItem implements Serializable {

    /**
     * 值
     */
    private String value;

    /**
     * 标签
     */
    private String label;

    public static <T, V, L> List<SelectItem> create(List<T> list, Function<T, V> valueFunc, Function<T, L> labelFunc) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(item -> {
            String value = String.valueOf(valueFunc.apply(item));
            String label = String.valueOf(labelFunc.apply(item));
            return new SelectItem(value, label);
        }).collect(Collectors.toList());
    }

}
