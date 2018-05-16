package com.patterncat.rpc.service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @description: 任务监控实体类
 * @param:
 * @return:
 * @author: ouy
 * @date: 2018/5/16 10:29
 */
public class MonitorInstance {
    private final ConcurrentHashMap<String, ServerInfoEntity> nodes = new ConcurrentHashMap<String, ServerInfoEntity>();
    private final ConcurrentHashMap<String, Long> nodeStatus = new ConcurrentHashMap<String, Long>();

    private long timeout = 9000;
    private static  class signgle{
        private static final MonitorInstance INSTANCE = new MonitorInstance();
    }

    private  MonitorInstance(){}

    public  static MonitorInstance getInstance(){
        return  signgle.INSTANCE;
    }

    /**
     *
     * @description: 返回监控对象
     * @param:
     * @return:
     * @author: ouy
     * @date: 2018/5/16 11:03
     */
    public ConcurrentHashMap<String, ServerInfoEntity> getNodes() {
        return nodes;
    }
    /**
     *
     * @description: 注册节点信息
     * @param:
     * @return:
     * @author: ouy
     * @date: 2018/5/16 11:04
     */
    public void registerNode(String nodeId, ServerInfoEntity nodeInfo) {
        nodes.put(nodeId, nodeInfo);
        nodeStatus.put(nodeId, System.currentTimeMillis());
    }

    /**
     *
     * @description: 删除节点信息
     * @param:
     * @return:
     * @author: ouy
     * @date: 2018/5/16 11:04
     */
    public void removeNode(String nodeID) {
        if (nodes.containsKey(nodeID)) {
            nodes.remove(nodeID);
        }
    }

    // 检测节点是否有效
    public boolean checkNodeValid(String key) {
        if (nodes.containsKey(key)&&nodeStatus.containsKey(key)){
            nodeStatus.put(key, System.currentTimeMillis());
            return true;
        }else {
            return false;
        }
    }

    // 删除所有失效节点
    public void removeInValidNode() {
        Iterator<Map.Entry<String, Long>> it = nodeStatus.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> e = it.next();
            if ((System.currentTimeMillis() - nodeStatus.get(e.getKey())) > timeout) {
                nodes.remove(e.getKey());
            }
        }
    }


}

