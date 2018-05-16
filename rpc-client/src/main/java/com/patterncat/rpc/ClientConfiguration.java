package com.patterncat.rpc;

import com.patterncat.rpc.proxy.RpcProxyFactory;
import com.patterncat.rpc.service.demo.MonitorServerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {


    @Bean
    public RpcProxyFactory rpcProxyFactory(){
        return new RpcProxyFactory();
    }

    /**
     * 也可以采用配置文件的方式
     * 如果不想自己proxy,可以像dubbo那样扩展schema
     * 或者自己scan指定包,在FactoryBean里头替换
     * @param rpcProxyFactory
     * @return
     */
    @Bean
    public MonitorServerService buildHelloService(RpcProxyFactory rpcProxyFactory){
        return rpcProxyFactory.proxyBean(MonitorServerService.class,5000/*timeout*/);
    }
}

