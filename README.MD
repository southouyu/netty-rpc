# netty-rpc

##参考
- [Zookeeper实现简单的分布式RPC框架](http://wujiu.iteye.com/blog/2207769)
- [使用Netty实现多路复用的client](http://my.oschina.net/u/719192/blog/268682)
- [netty 数据分包、组包、粘包处理机制（二）  ](http://blog.163.com/linfenliang@126/blog/static/127857195201210821145721)
- [netty 过长内容分成了多次发送 问题 LengthFieldBasedFrameDecoder使用](http://blog.csdn.net/zzycgfans/article/details/6732752)
- [netty之http部分handler的使用与超时控制handler](http://blog.csdn.net/fjslovejhl/article/details/11629825)
- [Netty 超时机制及心跳程序实现](http://waylau.com/netty-time-out-and-heartbeat/)
- [Spring bean 实现生命周期的三种解决方案](http://www.cnblogs.com/javawebsoa/archive/2013/05/19/3087394.html)
- [dubbo是如何“插入”到spring框架中的](http://blog.csdn.net/achilles12345/article/details/41789527)
- [Dubbo之旅--启动解析](http://blog.csdn.net/jnqqls/article/details/45371785)(`DubboBeanDefinitionParser#parse`)
- [spring hessian 自动扫描注入](http://www.thinksaas.cn/group/topic/352814/)
- [hessian-spring](https://github.com/mingpeng2live/hessian-spring)
- [rpc-spring](https://github.com/lindzh/rpc-spring)

##client端调用remote service的方式
- java config方式
```java
@Bean
    public HelloService buildHelloService(RpcProxyFactory rpcProxyFactory){
        return rpcProxyFactory.proxyBean(HelloService.class,100/*timeout*/);
    }
```
好处是，可以直接共用service-api的接口，不好的是java config稍微不如xml直观，不过适应就好

- xml方式
```xml
<bean id="helloService" class="com.patterncat.rpc.spring.SpringProxyFactoryBean">
		<property name="innerClassName">
			<value>com.patterncat.rpc.service.demo.HelloService</value>
		</property>
		<property name="timeoutInMillis">
			<value>200</value>
		</property>
	</bean>
```
这种方式就是配置感觉略微复杂点，不过还可以接受，具体可参照[navi](https://github.com/neoremind/navi)

- 更简洁的xml
参照dubbo的方式，自定义schema，[dubbo-config-spring](https://github.com/alibaba/dubbo/tree/master/dubbo-config/dubbo-config-spring)

- client再次定义接口
使用scanner的方式，client端再次写接口，然后client端扫描装配，重复定义，不建议用，具体可参照[rpc-spring](https://github.com/lindzh/rpc-spring/tree/master/src/main/java/com/linda/framework/rpc/spring/invoker)

##todo
- client的失败重连
- service mapping 采用注解形式
- 基于spring的远程代理参考[navi](https://github.com/neoremind/navi)
- 服务注册\发现\路由


