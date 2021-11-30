package com.gangling.scm.base.common.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1174060882411794381L;
    @Id
    @Column(name = "ID")
//    @GeneratedValue(generator = "JDBC")
    protected Long id;
}
