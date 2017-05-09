package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public String image() {
        return "imageAdmin";
    }

    @RequestMapping(value = "/netConfig", method = RequestMethod.GET)
    public String net() {
        return "netConfigAdmin";
    }

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public String policy() {
        return "policyAdmin";
    }

    @RequestMapping(value = "/qos", method = RequestMethod.GET)
    public String qos() {
        return "qosAdmin";
    }

    @RequestMapping(value = "/quota", method = RequestMethod.GET)
    public String quota() {
        return "quotaAdmin";
    }

    @RequestMapping(value = "/snapshots", method = RequestMethod.GET)
    public String snapshots() {
        return "snapshotsAdmin";
    }

    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public String instance() {
        return "instanceAdmin";
    }

    @RequestMapping(value = "/agent", method = RequestMethod.GET)
    public String agent() {
        return "agentAdmin";
    }
}
