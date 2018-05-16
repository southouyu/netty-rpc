package com.patterncat.rpc;

import com.patterncat.rpc.service.demo.MonitorServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by patterncat on 2016-04-12.
 */
@Component
@PropertySource({"classpath:nettyconfig.properties"})
public class DemoRpc implements CommandLineRunner{

    @Autowired
    private MonitorServerService monitorServerService;


    @Value("${client.ip}")
    private String ip;
    @Value("${server.name}")
    private String serverName;
    @Value("${connect.interval:5000}")
    private int interval;

    private  String id = UUID.randomUUID().toString();
    @Override
    public void run(String... strings) throws Exception {
        long startTime=0,lastHeartbeat=0;
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("id",id);
        paraMap.put("ip",ip);
        paraMap.put("serverName",serverName);
        while (true){
            startTime = System.currentTimeMillis();
            if(startTime - lastHeartbeat> interval){
                lastHeartbeat = startTime;
                monitorServerService.requestInfo(paraMap);
            }
        }
    }
}
