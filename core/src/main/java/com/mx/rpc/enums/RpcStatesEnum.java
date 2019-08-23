package com.mx.rpc.enums;

/**
 * @author mx
 * @date 2019/8/19 1:01 AM
 */
public enum RpcStatesEnum {

    FAIL(0, "调用RPC服务失败"),
    SUCCESS(1, "调用RPC服务成功"),
    SEVICE_NOT_EXISTS(-1, "请求的服务尚未注册");

    private Integer code;
    private String msg;

    RpcStatesEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
