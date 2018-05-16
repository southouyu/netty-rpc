package com.patterncat.rpc.proxy;

import com.patterncat.rpc.client.NettyClient;
import com.patterncat.rpc.client.NettyClientFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Created by patterncat on 2016-04-12.
 */
@Component
public class RpcProxyFactory {

    @Autowired
    private NettyClientFactory  nettyClientFactory;

    public <T> T proxyBean(Class<?> targetInterface,long timeoutInMillis){
        //创建client并进行连接
        NettyClient client = nettyClientFactory.get(targetInterface);
        //执行代理
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{targetInterface}, new RpcProxy(client,Pair.of(timeoutInMillis,TimeUnit.MILLISECONDS)));
    }
}
