package com.destiny.lock.interceptor;

import com.destiny.lock.api.BaseResponse;
import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;

public class ServiceExceptionInterceptor implements MethodInterceptor {
    public static Logger logger = LoggerFactory.getLogger(ServiceExceptionInterceptor.class);

    private void handlerBinderErrors(Object[] args) {
        if (args == null || args.length <= 0) {
            return;
        }
        for (Object obj : args) {
            if (obj != null && (obj instanceof Errors) && ((Errors) obj).hasErrors()) {
                throw new LockException(LockResponseCode.ILLEGAL_REQUEST_PARAM);
            }
        }
    }

    private BaseResponse handlerSuccessFlag(BaseResponse responseVO) {
        if (StringUtils.isEmpty(responseVO.getResult())) {
            responseVO.setResult(LockResponseCode.SUCCESS.getErrorCode());
        }
        if (StringUtils.isEmpty(responseVO.getResultString())) {
            responseVO.setResultString(LockResponseCode.SUCCESS.getErrorMsg());
        }
        return responseVO;
    }

    private BaseResponse fillBaseResponseVO(BaseResponse responseVO, String code, String message) {
        responseVO.setResult(code);
        responseVO.setResultString(message);
        return responseVO;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        String serviceName = invocation.getMethod().getDeclaringClass().getSimpleName();
        String methodName = invocation.getMethod().getName();
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (!BaseResponse.class.isAssignableFrom(returnType)) {
            return invocation.proceed();
        }
        try {
            logger.info("开始执行{}方法{}参数:{}", serviceName, methodName, args);
            handlerBinderErrors(args);//?��??????errors
            BaseResponse responseVO = (BaseResponse) invocation.proceed();
            responseVO = handlerSuccessFlag(responseVO == null ? (BaseResponse) returnType
                    .newInstance() : responseVO);
            logger.info("{}方法{}执行结果:{}", serviceName, methodName, responseVO);
            return responseVO;
        } catch (LockException e) {
            if (e.getErrorFlag()) {
                logger.error(String.format("%s方法%s返回错误码%s:%s", serviceName, methodName, e.getErrorCode(),
                        e.getErrorMsg()), e);
            } else {
                logger.warn(String.format("%s方法%s返回错误码%s:%s", serviceName, methodName, e.getErrorCode(),
                        e.getErrorMsg()), e);
            }
            return fillBaseResponseVO((BaseResponse) returnType.newInstance(), e.getErrorCode(),
                    e.getErrorMsg());
        } catch (Exception e) {
            logger.error(String.format("%s方法%s返回异常", serviceName, methodName), e);
            return fillBaseResponseVO((BaseResponse) returnType.newInstance(),
                    LockResponseCode.SYSTEM_ERROR.getErrorCode(),
                    LockResponseCode.SYSTEM_ERROR.getErrorMsg());
        }
    }
}
