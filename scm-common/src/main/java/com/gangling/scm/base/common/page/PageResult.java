package com.gangling.scm.base.common.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 *
 * @author shijian
 * @date 2021/10/26
 */
@Getter
@Setter
@ToString
public class PageResult<T> implements Serializable {

    /**
     * 结果
     */
    private List<T> result = Collections.emptyList();

    /**
     * 总计
     * @mock 100
     */
    private long total;

    public PageResult() {}

    public PageResult(List<T> result, long total) {
        this.result = result;
        this.total = total;
    }

    public static <T> PageResult<T> create(List<T> result, long total) {
        return new PageResult<>(result, total);
    }

}
