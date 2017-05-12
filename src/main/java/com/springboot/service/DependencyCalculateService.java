package com.springboot.service;

import com.springboot.model.API;
import com.springboot.model.Model;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Component
public class DependencyCalculateService {

    private String []services = {"nova", "glance", "keystone", "cinder"};
    private final int open = 0;

    public void update(Model model) {
        int count = model.getCount();
        double thres = model.getThreshold();
        //TODO 计算结果
        System.out.println(model.toString());
        // TODO 实施结果，修改OpenStack中的policy.json

        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() throws IOException {

//        String path = this.getClass().getResource("/").getPath() + "static/sourceFile/cinder.log";
//        System.out.println(path);

//        File f3 = new File(this.getClass().getResource("").getPath());
//        System.out.println(f3);

        File f4 = ResourceUtils.getFile("classpath:static/sourceFile/cinder.log");
        System.out.println(f4.getPath());

        RandomAccessFile file = new RandomAccessFile(f4, "r");
        String s;
        while ((s = file.readLine()) != null) {
            System.out.println(s);
        }
    }

    /**
     * 获取唯一的API
     * @throws IOException
     */
    public void getUniqueAPI() throws IOException {
        String testCase = "";
        Map<String, API> APIMap = new HashMap<String, API>();
        Set<String> APISet = new HashSet<String>();
        for (String service : services) {
            File file0 = ResourceUtils.getFile("classpath:static/sourceFile/" + service + ".log");
            RandomAccessFile file = new RandomAccessFile(file0, "r");
            String s;
            while ((s = file.readLine()) != null) {

                if (!s.startsWith("##") && !s.startsWith(" ")) {
                    if (!s.contains("%UUID%") && s.contains("UUID")) {
                        s = s.replace("UUID", "%UUID%");
                    }
                    String resource = getResource(s);
                    if (resource.equals(""))
                        continue;
                    API api = new API(service, s, resource, testCase);
                    APIMap.put(api.getBody(), api);
                    APISet.add(api.toString());
                }else if (s.startsWith("##") && s.endsWith("##")) {
                    testCase = s.replaceAll("##", "");
                }
            }
            file.close();
        }

        switch (open) {
            case 0:
                System.out.println("Unique API Map size: " + APIMap.size());
                System.out.println("Unique API Set size: " + APISet.size());
            case 1:
//                print(APIMap);
            case 2:
                dump(APIMap);
        }
    }

    /**
     * 把API写到文件
     * @param APIMap
     */
    private void dump(Map<String, API> APIMap) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/imac/Desktop/test.txt", true)));

            for (Map.Entry<String, API> entry : APIMap.entrySet()) {
                out.write(entry.getValue().toString() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输出API
     * @param APIMap
     */
    private void print(Map<String, API> APIMap) {
        for (Map.Entry<String, API> entry : APIMap.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
    }

    /**
     * 从API中获取其操作的资源
     * @param api API
     * @return 资源
     */
    private String getResource(String api) {
        String []list = api.split("\t");
        if (list.length == 2) {
            api = list[1];
            api = api.replaceAll("/%UUID%", "").replaceAll("/v\\d\\.?\\d?/", "");
            return api;
        }else {
            return "";
        }
    }

    // 根据是否在同一服务内计算
    private double calculate() {

        return 0.0;
    }

}
