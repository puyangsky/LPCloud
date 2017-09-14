package com.springboot.util;

import com.alibaba.fastjson.JSON;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Author:      puyangsky
 * Date:        17/9/14 下午12:23
 */
public class FileUtil {

    /**
     * 持久化对象到文件
     * @param filePath  文件路径
     * @param obj       对象
     */
    public static void dump(String filePath, Object obj) {
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false), "UTF-8"));
            br.write(JSON.toJSONString(obj));
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
}
