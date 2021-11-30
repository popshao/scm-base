package com.gangling.scm.base.common.rpc.result;

import java.io.Serializable;

/**
 * dubbo结果
 * @author shijian
 * @date 2021/10/26
 */
public class PlainResult<T> implements Serializable {
    private static final long serialVersionUID = -3688438694024330532L;

    /**
     * 响应编码&消息
     */
    private CodeMsg codeMsg;

    /**
     * 数据
     */
    private T data;

    public PlainResult() {
    }

    public T getData() {
        return data;
    }

    public T getDataOrDefault(T defData) {
        return null == data ? defData : data;
    }

    public void success(T data) {
        this.data = data;
        this.codeMsg = new CodeMsg("0", null);
    }

    public void failure(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public void failure(CodeMsg codeMsg, Object... msgParams) {
        this.codeMsg = new CodeMsg(codeMsg.getCode(), String.format(codeMsg.getMessage(), msgParams));
    }

    public Boolean isOk() {
        return (null != this.codeMsg) && (null != this.codeMsg.getCode()) && this.codeMsg.getCode().equals("0");
    }

    public String getCode() {
        return (null != this.codeMsg) ? this.codeMsg.getCode() : null;
    }

    public String getMessage() {
        return (null != this.codeMsg) ? this.codeMsg.getMessage() : null;
    }

    // 是否系统错误
    public Boolean sysError() {
        return !isOk() && getCode() != null && getCode().startsWith(SysErrCodes.SYS_ERR_CODE_PREFIX);
    }

    @Override
    public String toString() {
        return "PlainResult{" + "codeMsg=" + codeMsg + ", data=" + data + '}';
    }
}
