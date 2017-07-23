package com.springboot.util;

import com.alibaba.fastjson.JSON;
import com.springboot.model.Policy;

import java.io.*;
import java.util.List;

/**
 * Author:      puyangsky
 * Date:        17/7/24 上午12:50
 * Method:
 * Difficulty:
 */
public class JsonUtils {

    public static final String filePath = "/Users/imac/IdeaProjects/LPCloud/src/main/java/com/springboot/util/policy.json";

    /**
     * 将policy.json反序列化成Policy对象
     * @throws IOException
     */
    public static List<Policy> deserialize() throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        String line;
        String content;
        StringBuilder sb = new StringBuilder();
        while ((line = file.readLine()) != null) {
            sb.append(line);
        }
        content = sb.toString();
        return JSON.parseArray(content, Policy.class);
    }


    /**
     * 序列化Policy对象
     * @throws IOException
     */
    public static void serialize(List<Policy> policyList) throws IOException {
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false)));
        br.write(JSON.toJSONString(policyList));
        br.close();
    }


    /**
     * 增加rule
     * @param policy
     */
    public static void addRule(Policy policy) throws IOException {
        List<Policy> policyList = null;
        policyList = deserialize();

        if (policyList == null)
            return;

        if (policyList.contains(policy))
            return;

        policyList.add(policy);

        serialize(policyList);
    }


    /**
     * 删除rule
     * @param policy
     */
    public static void removeRule(Policy policy) throws IOException{
        List<Policy> policyList = null;
        policyList = deserialize();

        if (policyList == null)
            return;

        if (!policyList.contains(policy)) {
//            System.out.println("不存在");
            return;
        }

        policyList.remove(policy);

        serialize(policyList);
    }


    public static void main(String[] args) {
        try {
//            deserialize();
//            addRule(new Policy("FUCK","FUCK","FUCK"));
            removeRule(new Policy("FUCK","FUCK","FUCK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
