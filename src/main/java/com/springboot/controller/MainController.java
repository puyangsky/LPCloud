package com.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.*;

/**
 * Author: puyangsky
 * Date: 17/5/7
 */

@SpringBootApplication
@RequestMapping("/api")
public class MainController extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainController.class);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "SpringBoot");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/fuck", method = RequestMethod.GET)
    @ResponseBody
    String index() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);
    }
}