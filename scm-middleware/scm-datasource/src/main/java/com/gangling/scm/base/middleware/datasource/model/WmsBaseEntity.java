package com.gangling.scm.base.middleware.datasource.model;

import com.gangling.scm.base.common.entity.BaseEntity;
import lombok.Data;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * Created by huyan on 2021/3/15.
 * TIME: 17:28
 * DESC:
 */
@Data
public class WmsBaseEntity extends BaseEntity {

    @Column(name="CREATE_BY")
    protected String createBy;
    @Column(name="CREATE_TIME")
    @XmlTransient
    protected Date createTime;
    @Column(name="UPDATE_BY")
    protected String updateBy;
    @Column(name="UPDATE_TIME")
    @XmlTransient
    protected Date updateTime;
    @Version
    @Column(name="VERSION")
    protected Integer version;

}
