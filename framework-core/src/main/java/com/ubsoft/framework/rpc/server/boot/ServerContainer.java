
package com.ubsoft.framework.rpc.server.boot;

import com.alibaba.dubbo.container.Container;
import com.ubsoft.framework.core.conf.AppConfig;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerContainer implements Container {
    public static final String DEFAULT_SPRING_CONFIG = "classpath*:conf/server-context.xml";
    static ClassPathXmlApplicationContext context;

    public static ClassPathXmlApplicationContext getContext() {
        return context;
    }

    public void start() {

        context = new ClassPathXmlApplicationContext(DEFAULT_SPRING_CONFIG);
        context.start();
        //设置全局context
        AppConfig.sprintContext = context;
        //加载配置文件        
        String confPath = ServerContainer.class.getClassLoader().getResource("conf/config.xml").getPath();
        AppConfig.initConfig(confPath);
        //加载缓存
        String cachePath = ServerContainer.class.getClassLoader().getResource("conf/ehcache.xml").getPath();
        AppConfig.initCache(cachePath);
        ////加载数据模型
        String fdmPath = ServerContainer.class.getClassLoader().getResource("view/fdm/").getPath();
        AppConfig.initFdm(fdmPath);
        ////加载界面模型
        String formPath = ServerContainer.class.getClassLoader().getResource("view/form/").getPath();
        AppConfig.initForm(formPath);

    }

    public void stop() {
        try {
            if (context != null) {
                context.stop();
                context.close();
                context = null;
            }
        } catch (Throwable e) {

        }
    }


}