package com.springboot.service;

import com.alibaba.fastjson.JSON;
import com.springboot.model.Role;
import com.springboot.util.FileUtil;
import com.springboot.util.JsonUtils;
import com.springboot.util.RoleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * Author:      puyangsky
 * Date:        17/7/29 上午1:25
 */
@Component
public class RoleService implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Resource
    private RoleUtil roleUtil;

    private List<Role> roleList;
    private JsonUtils<Role> jsonUtils = new JsonUtils<>();
    private File roleFile;

    public List<Role> getRoleList() {
        if (roleList == null) {
            fillRoleList();
        }
        logger.info("get {} roles", roleList.size());
        return roleList;
    }


    public void addRole(Role role) {
        if (roleList == null) {
            fillRoleList();
        }

        if (roleList.contains(role)) {
            return;
        }

        roleList.add(role);
        logger.info("增加角色：" + role.toString());

        dumpRoles();
        logger.info("将角色写入文件：" + role.getRole());
    }


    public void addRoles(List<Role> roles) {
        if (roleList == null) {
            fillRoleList();
        }
        roles.forEach(role -> {
            if (!roleList.contains(role)) {
                roleList.add(role);
                logger.info("增加角色：" + role.toString());
            }
        });
        dumpRoles();
    }


    public void setRoleList(List<Role> roles) {
        roleList = roles;
        dumpRoles();
    }


    /**
     * 从文件中读取到角色列表
     */
    private void fillRoleList() {
        try {
            roleList = jsonUtils.deserialize(roleFile, Role.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dumpRoles() {
        if (roleList.size() == 0) {
            return;
        }
        try {
            jsonUtils.serialize(roleList, roleFile);
            logger.info("Dump " + roleList.size() + "roles into " + roleFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/imac/Desktop/data/role.json";
        JsonUtils<Role> jsonUtils = new JsonUtils<Role>();

        List<Role> roleList = jsonUtils.deserialize(new File(path), Role.class);
        logger.info(JSON.toJSONString(roleList));


        logger.info("=====================================");

        RandomAccessFile file = new RandomAccessFile(path, "r");
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = file.readLine()) != null) {
            sb.append(line);
        }

        logger.info(sb.toString());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String filePath = roleUtil.getName();
        roleFile = new File(filePath);
    }
}
