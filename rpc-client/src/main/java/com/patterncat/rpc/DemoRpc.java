package com.patterncat.rpc;

import com.patterncat.rpc.service.demo.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by patterncat on 2016-04-12.
 */
@Component
public class DemoRpc implements CommandLineRunner{

    @Autowired
    HelloService helloService;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(helloService.say("patterncat"));
    }
}
