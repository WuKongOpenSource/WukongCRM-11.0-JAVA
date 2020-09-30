package com.kakarote.core.exception;

import feign.Request;

/**
 * @author zhangzhiwei
 * feign异常
 */
public class FeignServiceException extends feign.FeignException {

    protected FeignServiceException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    protected FeignServiceException(int status, String message, Throwable cause, byte[] responseBody) {
        super(status, message, cause, responseBody);
    }

    public FeignServiceException(int status, String message) {
        super(status, message);
    }

    protected FeignServiceException(int status, String message, byte[] responseBody) {
        super(status, message, responseBody);
    }

    protected FeignServiceException(int status, String message, Request request, Throwable cause) {
        super(status, message, request, cause);
    }

    protected FeignServiceException(int status, String message, Request request, Throwable cause, byte[] responseBody) {
        super(status, message, request, cause, responseBody);
    }

    protected FeignServiceException(int status, String message, Request request) {
        super(status, message, request);
    }

    protected FeignServiceException(int status, String message, Request request, byte[] responseBody) {
        super(status, message, request, responseBody);
    }
}
