package com.gangling.scm.base.common.rpc;

import lombok.Data;

import java.io.Serializable;


@Data
public class BaseDTO implements Serializable {
    private static final long serialVersionUID = -6564950113078780056L;

    /**
     * 版本，供乐观锁使用，不需要赋值，框架会自动递增
     * @mock 1
     */
    private Integer version;
}

