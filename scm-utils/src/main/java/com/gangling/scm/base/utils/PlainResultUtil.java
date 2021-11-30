package com.gangling.scm.base.utils;

import com.alibaba.fastjson.JSON;
import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.common.rest.result.CommonMsgEnum;
import com.gangling.scm.base.common.rpc.result.CodeMsg;
import com.gangling.scm.base.common.rpc.result.PlainResult;
import com.gangling.scm.base.common.rpc.result.SysErrCodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class PlainResultUtil {
    public static <T> PlainResult<T> buildSuccessResult(T data) {
        PlainResult<T> plainResult = new PlainResult<T>();
        plainResult.success(data);
        return plainResult;
    }

    public static PlainResult buildFailureResult(CodeMsg codeMsg) {
        PlainResult plainResult = new PlainResult();
        plainResult.failure(codeMsg);
        return plainResult;
    }

    public static PlainResult buildFailureResult(CodeMsg codeMsg, Object... msgParams) {
        PlainResult plainResult = new PlainResult();
        plainResult.failure(codeMsg, msgParams);
        return plainResult;
    }

//    public static PlainResult buildFailureResult(BusinessException e)
//    {
//        Long code = null != e.getCode() ? e.getCode() : SysErrCodes.SYS_UNCHECKED_EXCEPTION.getCode();
//        PlainResult plainResult = new PlainResult();
//        plainResult.failure(new CodeMsg(code, e.getMessage()));
//        return plainResult;
//    }


    /**
     * 接口解封
     *
     * @param result 返回结果
     * @param <T>    返回数据类型
     * @return 解封装后的数据
     * @throws BusinessException 接口结果错误时抛的异常
     */
    public static <T> T unbox(PlainResult<T> result) throws BusinessException {

        if (result.isOk()) {
            return result.getData();
        } else if (StringUtils.isNotBlank(result.getMessage())
                && !result.getCode().equals(SysErrCodes.SYS_UNCHECKED_EXCEPTION.getCode())) {
            throw new BusinessException(result.getCode() == null ? String.valueOf(CommonMsgEnum.SYSTEM_ERROR.getCode() ): result.getCode().toString(), result.getMessage());
        } else {
            log.error(JSON.toJSONString(result));
            throw new BusinessException(String.valueOf(CommonMsgEnum.SYSTEM_ERROR.getCode()), CommonMsgEnum.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 接口解封
     *
     * @param result       返回结果
     * @param defaultValue 接口失败时返回默认数据
     * @param <T>          返回数据类型
     * @return 解封装后的数据
     */
    public static <T> T unbox(PlainResult<T> result, T defaultValue) {
        if (result.isOk()) {
            return result.getData();
        }
        log.error(JSON.toJSONString(result));
        return defaultValue;
    }

    /**
     * 断言为 ok, 否则直接抛出异常
     *
     * @param result
     * @throws BusinessException
     */
    public static void assertOk(PlainResult result) throws BusinessException {
        if (!result.isOk()) {
            if (result.getCode().equals(SysErrCodes.SYS_UNCHECKED_EXCEPTION.getCode())) {
                log.error(JSON.toJSONString(result));
                throw new BusinessException(String.valueOf(CommonMsgEnum.SYSTEM_ERROR.getCode()), CommonMsgEnum.SYSTEM_ERROR.getMessage());
            }
            throw new BusinessException(result.getCode() == null ? String.valueOf(CommonMsgEnum.SYSTEM_ERROR.getCode()): result.getCode().toString(), result.getMessage());
        }
    }

    /**
     * 断言为 ok, 否则直接抛出异常
     *
     * @param result
     * @param errorMsg
     * @throws BusinessException
     */
    public static void assertOk(PlainResult result, String errorMsg) throws BusinessException {
        String msg = result.getMessage();
        if (StringUtils.isNotBlank(errorMsg)) {
            msg = errorMsg;
        }
        if (!result.isOk()) {
            if (result.getCode().equals(SysErrCodes.SYS_UNCHECKED_EXCEPTION.getCode())) {
                log.error(JSON.toJSONString(result));
                throw new BusinessException(String.valueOf(CommonMsgEnum.SYSTEM_ERROR.getCode()), CommonMsgEnum.SYSTEM_ERROR.getMessage());
            }
            throw new BusinessException(result.getCode() == null ? String.valueOf(CommonMsgEnum.SYSTEM_ERROR.getCode()) : result.getCode().toString(), msg);
        }
    }
}
