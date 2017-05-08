package com.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.model.Model;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Author: puyangsky
 * Date: 17/5/7
 */

@SpringBootApplication
@RequestMapping("/api")
public class ApiController extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiController.class);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "SpringBoot");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    String index(HttpServletRequest request) {
        try {
            BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
            byte[] bytes = new byte[1024];
            bis.read(bytes);
            String s = new String(bytes);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    String test(@RequestBody Model model) {
        return model == null ? "" : model.toString();
    }


    public static void main(String[] args) {
        SpringApplication.run(ApiController.class, args);
    }
}