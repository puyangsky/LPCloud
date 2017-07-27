package com.springboot;

import com.springboot.util.PolicyUtil;
import com.springboot.util.RoleUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Author: puyangsky
 * Date: 17/5/7
 */

@SpringBootApplication
@EnableConfigurationProperties({PolicyUtil.class, RoleUtil.class})
@ComponentScan(basePackages = {"com.springboot"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}