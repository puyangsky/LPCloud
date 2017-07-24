package com.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.model.Policy;
import com.springboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author:      puyangsky
 * Date:        17/7/25 上午1:13
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/enforce", method = RequestMethod.POST)
    @ResponseBody
    public String auth(@RequestBody Policy requst) {
        JSONObject result = new JSONObject();
        result.put("result", authService.enforce(requst));
        return result.toString();
    }
}
