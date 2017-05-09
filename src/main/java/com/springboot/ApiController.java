package com.springboot;

import com.alibaba.fastjson.JSONObject;
import com.springboot.model.Model;
import com.springboot.model.User;
import com.springboot.service.UpdateService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    UpdateService updateService;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiController.class);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "LPCloud");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    String index(HttpServletRequest request) {
        try {
            BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
            byte[] bytes = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while (bis.read(bytes) !=0) {
                sb.append(new String(bytes));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    String test(@RequestBody Model model) {
        updateService.update(model);
        return model == null ? "" : model.toString();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    String login(HttpServletRequest request, @RequestBody User user) {
        if (user == null)
            return "fail";
        if (user.getEmail().equals("admin@osvt.net") && user.getPassword().equals("123456")){
            request.getSession().setAttribute("user", "admin");
            return "success";
        }else {
            return "fail";
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(ApiController.class, args);
    }
}