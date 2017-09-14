package com.springboot.controller;

import com.springboot.model.Model;
import com.springboot.model.Policy;
import com.springboot.model.Role;
import com.springboot.model.User;
import com.springboot.service.PolicyService;
import com.springboot.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    @Resource
    private PolicyService calcService;

    @Resource
    private RoleService roleService;

    @Resource
    private PolicyService policyService;

    @RequestMapping(value = "/addRole", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    String addRule(@RequestBody Role role) {
        try {
            roleService.addRole(role);
            return "success";
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "fail";
    }

    @RequestMapping(value = "/getRole", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<Role> getRole() {
        List<Role> roleList = roleService.getRoleList();
        if (roleList == null) return null;
        return roleList;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    String update(@RequestBody Model model) {
        if (model == null || model.getCount() <= 0) {
            return "Invalid parameter";
        }
        calcService.update(model);
        return model.toString();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    String login(HttpServletRequest request, @RequestBody User user) {
        if (user == null)
            return "fail";
        if (user.getEmail().equals("admin@osvt.net") && user.getPassword().equals("123")){
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


    @RequestMapping(value = "/getPolicy", method = RequestMethod.GET)
    @ResponseBody
    public List<Policy> getPolicyList(@RequestParam(name = "service") String serviceName) {
        if (StringUtils.isEmpty(serviceName)) {
            return null;
        }
        return policyService.getPolicyListByServiceName(serviceName);
    }
}
