package com.springboot.service;

import com.springboot.model.API;
import com.springboot.model.Model;
import com.springboot.model.Policy;
import com.springboot.model.Role;
import com.springboot.util.JsonUtils;
import com.springboot.util.PolicyUtil;
import com.springboot.util.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Component
public class DependencyCalculateService {

    private Logger logger = Logger.getLogger(DependencyCalculateService.class.getName());
    private String []services = {"nova", "glance", "keystone", "cinder"};
    private final int open = 0;
    private Map<String, API> APIMap = new HashMap<String, API>();
    private Set<String> APISet = new HashSet<String>();
    private Map<String, Double> scoreMap = new HashMap<String, Double>();
    private double threshold = 1.0;
    private double resourceWeight = 1/3.0; /* 资源占比重 */
    private double testCaseWeight = 1/3.0; /* 测试用例占比重 */
    private double serviceWeight = 1/3.0;  /* 服务占比重 */
    private List<List<String>> partitionResult = new ArrayList<List<String>>();

    @Autowired
    private PolicyUtil policyUtil;

    @Autowired
    private RoleUtil roleUtil;

    public boolean addRole(Role role) {
        String filePath = roleUtil.getName();
        JsonUtils<Role> jsonUtil = new JsonUtils<Role>();
        try {
            File file = new File(filePath);
            return jsonUtil.add(role, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean addPolicy(Policy policy) {
        String policyPath = policyUtil.getName();
        JsonUtils<Policy> jsonUtil = new JsonUtils<Policy>();
        try {
            File file = new File(policyPath);
            return jsonUtil.add(policy, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    // TODO 实施结果，修改OpenStack中的policy.json
    public void update(Model model) {
        int count = model.getCount();

        try {
            resolve(count);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取唯一的API
     * @throws IOException
     */
    public void getUniqueAPI() throws IOException {
        String testCase = "";

        int line = 0;
        for (String service : services) {
            File file0 = ResourceUtils.getFile("classpath:static/sourceFile/" + service + ".log");
            RandomAccessFile file = new RandomAccessFile(file0, "r");
            String s;
            while ((s = file.readLine()) != null) {

                line++;

                if (!s.startsWith("##") && !s.startsWith(" ")) {
                    if (!s.contains("%UUID%") && s.contains("UUID")) {
                        s = s.replace("UUID", "%UUID%");
                    }
                    String resource = getResource(s);
                    if (resource.equals(""))
                        continue;
                    API api = new API(service, s, resource, testCase);
                    APIMap.put(api.getBody(), api);
                    APISet.add(api.getBody());
                }else if (s.startsWith("##") && s.endsWith("##")) {
                    testCase = s.replaceAll("##", "");
                }
            }
            file.close();
        }

        System.out.println("行数：" + line);

        switch (open) {
            case 0:
                System.out.println("Unique API Map size: " + APIMap.size());
                System.out.println("Unique API Set size: " + APISet.size());
                for (String k : APISet) {
                    System.out.println(k);
                }
                break;
            case 1:
                print(APIMap);
                break;
            case 2:
                dump(APIMap);
                break;
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

    /**
     * 处理数据
     * @param count 预期的管理员数量
     * @throws IOException
     */
    private void resolve(int count) throws IOException, InterruptedException {
        if (APISet.size() == 0) {
            this.getUniqueAPI();
        }
        fillMap();
        splitMap();
        int c = dumpResult();
        while (c != count) {
            if (c > count) {
                threshold += 0.1;
            } else if (c < count) {
                threshold -= 0.1;
            }
            logger.warning("Get " + c + " Administrators! Retrying...Threshold is now: " + threshold);
            Thread.sleep(5000);
            splitMap();
            c = dumpResult();
        }
    }

    /**
     * 填充scoreMap
     */
    private void fillMap() {
        for (String body : APISet) {
            API api = APIMap.get(body);
            for (String body1 : APISet) {
                if (body.equals(body1)) continue;
                API api1 = APIMap.get(body1);
                String key = api.getBody() + "|" + api1.getBody();
                String key1 = api1.getBody() + "|" + api.getBody();
                if (scoreMap.containsKey(key1)) continue;
                Double weight = 0.0;
                weight += resourceWeight * (api.getResource().equals(api1.getResource()) ? 1 : 0);
                weight += serviceWeight * (api.getService().equals(api1.getService()) ? 1 : 0);
                weight += testCaseWeight * (api.getTestCase().equals(api1.getTestCase()) ? 1 : 0);
                scoreMap.put(key, weight);
            }
        }
        logger.info("Map has been filled!");
    }

    /**
     * 划分scoreMap，填充partitionResult
     */
    private void splitMap() {
        if (scoreMap.size() == 0) return;
        for (Map.Entry<String, Double> entry : scoreMap.entrySet()) {
            String apis = entry.getKey();
            if (entry.getValue() > threshold) {
                String[] api0 = apis.split("\\|");
                String api1 = api0[0];
                String api2 = api0[1];
                List<String> list1 = null, list2 = null;
                for (List<String> strings : partitionResult) {
                    if (strings.contains(api1)) {
                        list1 = strings;
                    }

                    if (strings.contains(api2)) {
                        list2 = strings;
                    }
                }

                if (list1 == null && list2 == null) {
                    list1 = new ArrayList<String>();
                    list1.add(api1);
                    list1.add(api2);
                    partitionResult.add(list1);
                }else if (list1 != null && list2 != null && list1 != list2) {
                    list1.addAll(list2);
                    partitionResult.remove(list2);
                }else if (list1 != null && list2 == null) {
                    list1.add(api2);
                }else if (list1 == null) {
                    list2.add(api1);
                }
            }
        }
        logger.info("Split done");
    }

    /**
     * 输出结果到json中
     */
    private int dumpResult() {

        for (List<String> list : partitionResult) {
            for (String api : list) {
                System.out.println(api);
            }
            System.out.println("----------------");
            System.out.println("----------------");
        }

        return partitionResult.size();
    }

}
