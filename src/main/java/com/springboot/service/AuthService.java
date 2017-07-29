package com.springboot.service;

import com.springboot.model.Policy;
import com.springboot.util.JsonUtils;
import com.springboot.util.PolicyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Author:      puyangsky
 * Date:        17/7/24 下午11:48
 */
@Component
public class AuthService {

    @Resource
    private PolicyUtil policyUtil;

    public boolean enforce(Policy request) {
        String policyPath = policyUtil.getName();
        try {
            File policyFile = ResourceUtils.getFile("classpath:static/" + policyPath);
            JsonUtils<Policy> jsonUtils = new JsonUtils<Policy>();
            List<Policy> policies = jsonUtils.deserialize(policyFile, request);
            if (policies.contains(request)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void test() throws IOException {
        File policyFile = ResourceUtils.getFile("classpath:static/role.json");
        RandomAccessFile file = new RandomAccessFile(policyFile, "r");
        String s;
        while ((s = file.readLine())!= null) {
            System.out.println(s);
        }
    }
}
