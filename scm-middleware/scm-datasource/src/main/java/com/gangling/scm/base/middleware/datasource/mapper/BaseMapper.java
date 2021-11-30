package com.gangling.scm.base.middleware.datasource.mapper;

import com.gangling.scm.base.middleware.datasource.mapper.ids.ExtIdsMapper;
import com.gangling.scm.base.middleware.datasource.mapper.oracle.BaseInsertMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;

public interface BaseMapper<T> extends
        BaseSelectMapper<T>,
        BaseInsertMapper<T>,
        BaseUpdateMapper<T>,
        BaseDeleteMapper<T>,
        Marker,
        IdsMapper<T>,
        ExtIdsMapper<T> {

}