package com.patterncat.rpc.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by patterncat on 2016-04-12.
 */
@Component
@PropertySource({"classpath:nettyconfig.properties"})
public class NettyClientFactory {

    private static ConcurrentHashMap<Class<?>,NettyClient> serviceClientMap = new ConcurrentHashMap<Class<?>, NettyClient>();
    @Value("${server.ip}")
    private  String serverIp;
    @Value("${server.port}")
    private  int serverPort;

    public  NettyClient get(Class<?> targetInterface){
        NettyClient client = serviceClientMap.get(targetInterface);
        if(client != null && !client.isClosed()){
            return client;
        }
        //connect
        NettyClient newClient = new NettyClient();
        //TODO get from service registry
        newClient.connect(new InetSocketAddress(serverIp,serverPort));
        serviceClientMap.putIfAbsent(targetInterface,newClient);
        return newClient;
    }


}
