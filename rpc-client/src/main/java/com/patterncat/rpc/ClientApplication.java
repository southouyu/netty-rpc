package com.patterncat.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Created by patterncat on 2016-04-11.
 */
@SpringBootApplication
public class ClientApplication {

//    @Component
//    public static class BeanScannerConfigurer implements BeanFactoryPostProcessor, ApplicationContextAware {
//
//        private ApplicationContext applicationContext;
//
//        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext = applicationContext;
//        }
//
//        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//            RpcScanner scanner = new RpcScanner((BeanDefinitionRegistry) beanFactory);
//            scanner.setResourceLoader(this.applicationContext);
//            scanner.scan("com.patterncat.rpc.");
//        }
//    }

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(ClientApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }
}
