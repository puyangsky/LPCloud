package com.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.model.Model;
import com.springboot.model.User;
import com.springboot.service.DependencyCalculateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Controller
@RequestMapping("/api")
public class ApiController {
    @Resource
    private DependencyCalculateService dependencyCalculateService;

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
    String update(@RequestBody Model model) {
//        String path = request.getServletContext().getContextPath();
//        System.out.println(path);
        dependencyCalculateService.update(model);
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

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    String logout(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().setAttribute("user", null);
            return "success";
        }else {
            return "fail";
        }
    }

    @RequestMapping(value = "/append", method = RequestMethod.POST)
    @ResponseBody
    String addApi(HttpServletRequest request) {
        return "";
    }
}
