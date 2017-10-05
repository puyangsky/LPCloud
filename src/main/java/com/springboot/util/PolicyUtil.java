package com.springboot.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author:      puyangsky
 * Date:        17/7/25 上午12:03
 */
@ConfigurationProperties(prefix = "policy")
public class PolicyUtil {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
