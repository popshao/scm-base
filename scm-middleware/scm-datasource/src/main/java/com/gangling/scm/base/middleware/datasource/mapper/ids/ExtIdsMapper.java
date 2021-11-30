/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gangling.scm.base.middleware.datasource.mapper.ids;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.Date;
import java.util.List;

/**
 * 通用Mapper接口,基础查询
 *
 * @param <T> 不能为空
 */
@RegisterMapper
public interface ExtIdsMapper<T> {

    @InsertProvider(type = ExtIdsProvider.class, method = "dynamicSQL")
    int logicalDeleteByIds(@Param("ids") String ids, @Param("updateBy") String updateBy, @Param("updateTime") String updateTime);

    @InsertProvider(type = ExtIdsProvider.class, method = "dynamicSQL")
    int updateByIds(@Param("record") T record, @Param("ids") String ids);

    @InsertProvider(type = ExtIdsProvider.class, method = "dynamicSQL")
    int insertList(@Param("list") List<T> list);

    @SelectProvider(type = ExtIdsProvider.class, method = "dynamicSQL")
    List<T> listByKeyList(@Param("keys") String keys, @Param("columnName") String columnName, @Param("isDeleted") Integer isDelete);
}