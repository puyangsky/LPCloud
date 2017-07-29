package com.springboot.service;

import com.alibaba.fastjson.JSON;
import com.springboot.model.Role;
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

    public void dumpRole() {
        if (roleList == null) return;

        String filePath = roleUtil.getName();
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false), "UTF-8"));
            br.write(JSON.toJSONString(roleList));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
