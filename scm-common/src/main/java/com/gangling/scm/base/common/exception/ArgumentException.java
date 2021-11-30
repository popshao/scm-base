package com.gangling.scm.base.common.exception;

import com.gangling.scm.base.common.ConstantValue;

public class ArgumentException extends RuntimeException {
    private static final long serialVersionUID = -3010597632397639532L;
    private String code;
    private String message;

    public ArgumentException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ArgumentException(String message) {
        this.code = ConstantValue.ARGUMENT_EXCEPTION_CODE;
        this.message = message;
    }

    public ArgumentException() {
        super();
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(String message, Throwable cause) {
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
}
