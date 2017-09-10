package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: puyangsky
 * Date: 17/5/7
 */

@Controller
@RequestMapping("/")
public class RestController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            return "login";
        }
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("user") != null &&
                session.getAttribute("user").equals("admin")){
            return "index";
        }
        return "login";
    }
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String toIndex(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            return "login";
        }
        return "index";
    }

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public String getPolicy(@RequestParam(name = "name") String name,
                            ModelMap model) {
        model.put("adminName", name);
        return "policy";
    }
}
