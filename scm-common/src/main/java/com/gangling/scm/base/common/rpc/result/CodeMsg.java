package com.gangling.scm.base.common.rpc.result;

import java.io.Serializable;

/**
 * 响应编码&消息
 * @author shijian
 * @date 2021/10/15
 */
public class CodeMsg implements Serializable {
    private static final long serialVersionUID = -7241354606203046592L;

    /**
     * code
     * @mock 0
     */
    private String code;

    /**
     * message
     * @mock 成功
     */
    private String message;

    public CodeMsg() {
    }

    public CodeMsg(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CodeMsg{" + "code=" + code + ", message='" + message + '\'' + '}';
    }
}
