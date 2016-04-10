package com.patterncat.rpc.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;

/**
 * Created by patterncat on 2016-04-10.
 */
public class SpringProxyFactoryBean implements FactoryBean, InitializingBean {

    private int timeoutInMills = 1000; //默认1s



    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{serviceInterface}, new NaviInvocationProxy());
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
