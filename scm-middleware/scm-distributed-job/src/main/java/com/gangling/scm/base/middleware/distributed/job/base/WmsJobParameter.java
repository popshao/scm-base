package com.gangling.scm.base.middleware.distributed.job.base;

import lombok.Data;

import java.util.List;

@Data
public class WmsJobParameter {
    // 指定仓库
    private List<Long> warehouseIdList;
    // 开始时间戳
    private Long startTimeStamp;
    // 结束时间戳
    private Long endTimeStamp;
}
