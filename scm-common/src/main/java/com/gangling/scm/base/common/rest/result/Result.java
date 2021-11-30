package com.gangling.scm.base.common.rest.result;

import com.gangling.scm.base.common.rpc.result.CodeMsg;

import java.io.Serializable;

/**
 * rest结果
 *
 * @author shijian
 * @date 2021/10/15
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 4904786648892436996L;

    /**
     * code
     *
     * @mock 1
     */
    private int ret;

    /**
     * msg
     *
     * @mock 成功
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
    /**
     * 状态
     *
     * @mock 1
     */
    private Integer status;

    private Result() {
    }

    public Result(CommonMsgEnum commonMsgEnum, T data) {
        this.ret = commonMsgEnum.getCode();
        this.msg = commonMsgEnum.getMessage();
        this.data = data;
        this.status = CommonMsgEnum.SUCCESS.getCode() == ret ? 1 : 0;
    }

    public Result(CodeMsg codeMsg, T data) {
        this.ret = "0".equals(codeMsg.getCode()) ? 1 : 0;
        this.msg = codeMsg.getMessage();
        this.data = data;
        this.status = CommonMsgEnum.SUCCESS.getCode() == ret ? 1 : 0;
    }

    public Result(int ret, String msg, T data) {
        this.ret = ret;
        this.msg = msg;
        this.data = data;
        this.status = CommonMsgEnum.SUCCESS.getCode() == ret ? 1 : 0;
    }

    // begin success method
    public static <T> Result<T> wrapDefaultSuccessResult() {
        return Result.wrapSuccessResult(CommonMsgEnum.SUCCESS);
    }

    public static <T> Result<T> wrapDefaultSuccessResult(T data) {
        return Result.wrapSuccessResult(CommonMsgEnum.SUCCESS, data);
    }

    public static <T> Result<T> wrapSuccessResult(CommonMsgEnum commonMsgEnum) {
        return Result.wrapResult(commonMsgEnum, null);
    }

    public static <T> Result<T> wrapSuccessResult(CommonMsgEnum commonMsgEnum, T data) {
        return Result.wrapResult(commonMsgEnum, data);
    }
    // end

    // begin failed method
    public static <T> Result<T> wrapDefaultErrorResult() {
        return Result.wrapSuccessResult(CommonMsgEnum.SERVER_ERROR);
    }

    public static <T> Result<T> wrapErrorResult(CodeMsg codeMsg) {
        return Result.wrapResult(codeMsg, null);
    }

    public static <T> Result<T> wrapErrorResult(CommonMsgEnum commonMsgEnum) {
        return Result.wrapResult(commonMsgEnum, null);
    }

    public static <T> Result<T> wrapErrorResult(int code, String message) {
        return Result.wrapResult(code, message, null);
    }

    public static <T> Result<T> wrapErrorResult(int code, String message, T data) {
        return Result.wrapResult(code, message, data);
    }
    // end

    private static <T> Result<T> wrapResult(CommonMsgEnum commonMsgEnum, T data) {
        return new Result<T>(commonMsgEnum, data);
    }

    private static <T> Result<T> wrapResult(CodeMsg codeMsg, T data) {
        return new Result<T>(codeMsg, data);
    }

    private static <T> Result<T> wrapResult(int code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
