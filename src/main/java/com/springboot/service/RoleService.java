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

        dumpRole();
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

        dumpRole();
    }



    /**
     * 把内存中的角色列表写到文件中去
     */
    public void dumpRole() {
        if (roleList == null) return;

        String filePath = roleUtil.getName();

        FileUtil.dump(filePath, roleList);
    }

    /**
     * 从文件中读取到角色列表
     */
    private void fillRoleList() {
        String filePath = roleUtil.getName();
        JsonUtils<Role> jsonUtils = new JsonUtils<Role>();
        try {
            roleList = jsonUtils.deserialize(new File(filePath), new Role());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/imac/Desktop/data/role.json";
        JsonUtils<Role> jsonUtils = new JsonUtils<Role>();

        List<Role> roleList = jsonUtils.deserialize(new File(path), new Role());
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
