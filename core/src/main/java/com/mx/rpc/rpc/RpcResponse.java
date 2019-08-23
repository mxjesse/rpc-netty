package com.mx.rpc.rpc;

import com.mx.rpc.common.message.Body;

import java.io.Serializable;

/**
 * @author mx
 * @date 2019/8/14 7:03 PM
 */
public class RpcResponse implements Body, Serializable {

    private static final long serialVersionUID = 5562231982700102478L;

    /**
     * 请求id
     */
    private int requestId;

    /**
     * rpc调用结果
     */
    private Object result;

    /**
     * 错误信息
     */
    private String errorMsg;

    public RpcResponse() {
    }

    public RpcResponse(int requestId, Object result, String errorMsg) {
        this.requestId = requestId;
        this.result = result;
        this.errorMsg = errorMsg;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId=" + requestId +
                ", result=" + result +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
