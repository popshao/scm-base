package com.gangling.scm.base.common.page;

import com.gangling.scm.base.common.exception.ArgumentException;
import com.gangling.scm.base.common.rpc.RpcParam;
import lombok.Data;

@Data
public class BasePageParam extends RpcParam {
    public static final int MAX_PAGE_SIZE = 100;
    private static final long serialVersionUID = -1255779614645006963L;
    /**
     * 页码
     *
     * @mock 1
     */
    private int pageNo = 1;
    /**
     * 页面大小
     *
     * @mack 10
     */
    private int pageSize = 20;
    private int startRecord;

    public void setPageSize(int pageSize) {
        if (pageSize > MAX_PAGE_SIZE) {
            throw new ArgumentException("参数不合法");
        }
        this.pageSize = pageSize;
    }

    public void setPageSizeForce(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRecord() {
        startRecord = (pageNo - 1) * pageSize;
        return startRecord;
    }
}
