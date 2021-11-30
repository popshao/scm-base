package com.gangling.scm.base.common.rest.result;

/**
 * 返回信息 Enum
 */
public enum CommonMsgEnum {

    //基本信息
    ERROR(-1, "操作失败"),
    SUCCESS(1, "操作成功"),
    SERVER_ERROR(99999, "接口异常"),

    USER_NOT_LOGIN(20001, "用户未登录"),
    USER_OR_PWD_ERROR(20002, "账号或密码不正确"),
    NEED_PERMISSIONS(20003, "没有相应的权限"),

    NUMBER_TRANSFER_ERROR(20101, "数字转换异常"),
    DATE_TRANSFER_ERROR(20102, "日期转换异常"),

    INSERT_FAILED(20201, "插入数据失败"),
    MODIFY_FAILED(20202, "更新数据失败"),
    DELETE_FAILED(20203, "删除数据失败"),

    SIGN_EXCEPTION(20301, "签名异常"),
    TOO_MANY_RESULTS_ERROR(20302, "查询数据量超过阈值"),
    REPEATED_REQUEST(20303, "请求重复提交"),
    SERVICE_CALL_FAILED(20304, "服务调用失败"),
    TIME_OUT(20305, "请求超时"),
    SYSTEM_ERROR(20306, "系统繁忙,请稍后再试"),
    SYNC_LOCK_FAIL(20307, "多人同时操作,请稍后重试"),
     ;

    private int code;

    private String message;

    CommonMsgEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public Long getCodeLong() {
        return Long.valueOf(code);
    }

    public String getMessage() {
        return message;
    }

}
