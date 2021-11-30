package com.gangling.scm.base.common.rpc.result;

public class SysErrCodes {
    public static final String SYS_ERR_CODE_PREFIX = "200";

    public static final CodeMsg SYS_ERR = new CodeMsg("20001001", "系统错误");
    public static final CodeMsg SYS_ILLEGAL_PARAM = new CodeMsg("20001002", "参数错误:%s");
    public static final CodeMsg SYS_UNCHECKED_EXCEPTION = new CodeMsg("20001003", "unchecked exception:%s");
    public static final CodeMsg SYS_INTERFACE_DEMOTION_EXCEPTION = new CodeMsg("20001004", "服务降级"); // 在调用之前, 降级非核心服务 MockClusterInvoker
    public static final CodeMsg SYS_LIMITING_EXCEPTION = new CodeMsg("20001005", "触发限流");
}
