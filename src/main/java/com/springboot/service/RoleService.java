package com.springboot.service;

import com.alibaba.fastjson.JSON;
import com.springboot.model.Role;
import com.springboot.util.FileUtil;
import com.springboot.util.JsonUtils;
import com.springboot.util.RoleUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * Author:      puyangsky
 * Date:        17/7/29 上午1:25
 */
@Component
public class RoleService {

    @Resource
    private RoleUtil roleUtil;

    private List<Role> roleList;
    private JsonUtils<Role> jsonUtils;
    private File roleFile;

    public List<Role> getRoleList() {
        if (roleList == null) {
            fillRoleList();
        }
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
        System.out.println("增加角色：" + role.toString());

        dumpRoles();
        System.out.println("将角色写入文件：" + role.getRole());
    }


    public void addRoles(List<Role> roles) {
        if (roleList == null) {
            fillRoleList();
        }
        roles.forEach(role -> {
            if (!roleList.contains(role)) {
                roleList.add(role);
                System.out.println("增加角色：" + role.toString());
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
        String filePath = roleUtil.getName();
        jsonUtils = new JsonUtils<Role>();
        roleFile = new File(filePath);
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
            System.out.println("Dump " + roleList.size() + "roles into " + roleFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/imac/Desktop/data/role.json";
        JsonUtils<Role> jsonUtils = new JsonUtils<Role>();

        List<Role> roleList = jsonUtils.deserialize(new File(path), Role.class);
        System.out.println(JSON.toJSONString(roleList));


        System.out.println("=====================================");

        RandomAccessFile file = new RandomAccessFile(path, "r");
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = file.readLine()) != null) {
            sb.append(line);
        }

        System.out.println(sb.toString());
    }
}
