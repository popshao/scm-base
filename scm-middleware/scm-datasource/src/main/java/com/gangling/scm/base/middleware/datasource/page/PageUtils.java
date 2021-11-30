package com.gangling.scm.base.middleware.datasource.page;

import com.gangling.scm.base.common.exception.ArgumentException;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class PageUtils {
    public static <T> List<T> queryAllPage(int pageSize, ISelect select) {
        if (pageSize > 1000) {
            throw new ArgumentException("pageSize不能超过1000");
        }

        List<T> result = Lists.newArrayList();
        int pageNum = 1;
        PageInfo<T> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);
        while (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            result.addAll(pageInfo.getList());
            if (!pageInfo.isHasNextPage()) {
                break;
            }

            pageNum++;
            pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);
        }
        return result;
    }
}
