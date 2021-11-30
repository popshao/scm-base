package com.gangling.scm.base.common.exception;

import com.gangling.scm.base.common.ConstantValue;
import com.gangling.scm.base.common.rest.result.CommonMsgEnum;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 8813873966359101346L;
    private String code;
    private String message;

    public BusinessException(String message) {
        this.code = ConstantValue.BUSINESS_EXCEPTION_CODE;
        this.message = message;
    }

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return this.code + ": " + this.message;
    }
}
