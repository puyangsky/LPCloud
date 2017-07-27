package com.springboot;

import com.springboot.model.Role;
import com.springboot.util.JsonUtils;
import com.springboot.util.RoleUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

/**
 * Author:      puyangsky
 * Date:        17/7/28 上午2:33
 */

public class JsonUtilsTest extends BaseTest{

    @Autowired
    private RoleUtil roleUtil;

    @Test
    public void test() throws IOException {
        String filePath = roleUtil.getName();
        JsonUtils<Role> jsonUtils = new JsonUtils<Role>();
        File file = ResourceUtils.getFile("classpath:static/" + filePath);
        System.out.println(file.getAbsolutePath());
        jsonUtils.add(new Role("test", "test", "test", "test"), file);
    }
}
