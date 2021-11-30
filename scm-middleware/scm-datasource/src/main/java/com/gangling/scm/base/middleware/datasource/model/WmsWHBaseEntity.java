package com.gangling.scm.base.middleware.datasource.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by huyan on 2021/3/15.
 * TIME: 17:30
 * DESC:
 */
@MappedSuperclass
@Data
public class WmsWHBaseEntity extends WmsBaseEntity {

    @Column(name="WAREHOUSE_ID")
    protected Long warehouseId;


}
