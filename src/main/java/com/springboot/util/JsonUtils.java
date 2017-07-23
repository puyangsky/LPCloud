package com.springboot.util;

import com.alibaba.fastjson.JSON;
import com.springboot.model.Policy;

import java.io.IOException;
import java.io.RandomAccessFile;
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
        StringBuffer sb = new StringBuffer();
        while ((line = file.readLine()) != null) {
            sb.append(line);
        }
        content = sb.toString();
        List<Policy> policyList = JSON.parseArray(content, Policy.class);
        System.out.println(policyList.size());
        return policyList;
    }


    /**
     * 序列化Policy对象
     * @throws IOException
     */
    public static void serialize(List<Policy> policyList) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.write(JSON.toJSONBytes(policyList));
    }


    /**
     * 增加rule
     * @param policy
     */
    public static void addRule(Policy policy) {
        List<Policy> policyList = null;
        try {
            policyList = deserialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (policyList == null)
            return;

        if (policyList.contains(policy))
            return;

        policyList.add(policy);

        try {
            serialize(policyList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除rule
     * @param policy
     */
    public static void removeRule(Policy policy) {
        List<Policy> policyList = null;
        try {
            policyList = deserialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (policyList == null)
            return;

        if (!policyList.contains(policy))
            return;

        policyList.remove(policy);

        try {
            serialize(policyList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            deserialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
