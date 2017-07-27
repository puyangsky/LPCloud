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
public class JsonUtils<T> {

    /**
     * 将policy.json反序列化成Policy对象
     * @throws IOException
     */
    public List<T> deserialize(File sourceFile, T t) throws IOException {
        RandomAccessFile file = new RandomAccessFile(sourceFile, "r");
        String line;
        String content;
        StringBuilder sb = new StringBuilder();
        while ((line = file.readLine()) != null) {
            sb.append(line);
        }
        content = sb.toString();
        return (List<T>) JSON.parseArray(content, t.getClass());
    }


    /**
     * 序列化T对象
     * @throws IOException
     */
    public void serialize(List<T> list, File file) throws IOException {
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
        br.write(JSON.toJSONString(list));
        br.close();
    }


    /**
     * 增加t
     * @param
     */
    public boolean add(T t, File file) throws IOException {
        List<T> list = deserialize(file, t);

        if (list == null)
            return false;

        if (list.contains(t))
            return false;

        list.add(t);
        serialize(list, file);
        return true;
    }


    /**
     * 删除t
     */
    public boolean remove(T t, File file) throws IOException{
        List<T> list = deserialize(file, t);
        if (list == null || !list.contains(t))
            return true;

        try {
            list.remove(t);
            serialize(list, file);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
