package com.mx.rpc.common.config;

import com.mx.rpc.common.coder.Coder;
import com.mx.rpc.common.coder.DefaultCoder;

/**
 * 配置类父类
 *
 * @author mx
 * @date 2019/8/14 3:20 PM
 */
public class Configuration {

    /**
     * 获取编解码器
     */
    private Coder coder = DefaultCoder.getInstance();

    public Coder getCoder() {
        return coder;
    }

    public void setCoder(Coder coder) {
        this.coder = coder;
    }
}
