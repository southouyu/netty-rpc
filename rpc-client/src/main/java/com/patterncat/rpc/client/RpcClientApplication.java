package com.patterncat.rpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by patterncat on 2016-04-09.
 */
@SpringBootApplication
public class RpcClientApplication {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(RpcClientApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }
}
