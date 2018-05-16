package com.patterncat.rpc.service;


import com.patterncat.rpc.common.annotation.ServiceExporter;
import com.patterncat.rpc.service.demo.MonitorServerService;

import java.util.Map;

/**
 * Created by patterncat on 2016/4/6.
 */
@ServiceExporter(value = "monitorServer",targetInterface = MonitorServerService.class,debugAddress = "127.0.0.1:9090")
public class MonitorServerServiceImpl implements MonitorServerService{

    @Override
    public String requestInfo(Map<String,String> monitorInfo) {
        ServerInfoEntity serverInfoEntity = new ServerInfoEntity();
        serverInfoEntity.setServerIp(monitorInfo.get("ip"));
        serverInfoEntity.setServerName(monitorInfo.get("serverName"));
        MonitorInstance monitorInstance = MonitorInstance.getInstance();
        //每次调用都会清除无效节点
        monitorInstance.removeInValidNode();
        //注册更新最新节点状态
        monitorInstance.registerNode(monitorInfo.get("id"),serverInfoEntity);
        System.out.println("id:"+monitorInfo.get("id")+" ip:"+monitorInfo.get("ip")+" ; serverName:"+monitorInfo.get("serverName"));
        return "hi:";
    }

}
