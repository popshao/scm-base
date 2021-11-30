package com.gangling.scm.base.common.exception;

import com.gangling.scm.base.common.ConstantValue;

public class ServerException extends RuntimeException {
    private static final long serialVersionUID = 8888873966359101346L;
    private String code;
    private String message;

    public ServerException(String message) {
        this.code = ConstantValue.SERVER_EXCEPTION_CODE;
        this.message = message;
    }

    public ServerException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServerException() {
        super();
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause) {
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
