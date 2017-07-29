package com.springboot.interceptor;

import com.springboot.service.RoleService;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * Author:      puyangsky
 * Date:        17/7/29 上午2:09
 * Method:
 * Difficulty:
 */
@Component
public class ApplicationEventListener implements ApplicationListener {

    RoleService roleService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量
        }
        else if (applicationEvent instanceof ApplicationPreparedEvent) { // 初始化完成 }
        }
        else if (applicationEvent instanceof ContextRefreshedEvent) { // 应用刷新 }
        }
        else if (applicationEvent instanceof ApplicationReadyEvent) {// 应用已启动完成}
        }
        else if (applicationEvent instanceof ContextStartedEvent) { //应用启动，需要在代码动态添加监听器才可捕获 }
        }
        else if (applicationEvent instanceof ContextStoppedEvent) { // 应用停止 }
            //TODO 在关闭之前把list持久化
            System.out.println("要关闭啦");

        }
        else if (applicationEvent instanceof ContextClosedEvent) { // 应用关闭 }
        }
        else{}
    }
}
