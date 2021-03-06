package com.springboot.service;

import com.springboot.model.API;
import com.springboot.model.Model;
import com.springboot.model.Policy;
import com.springboot.model.Role;
import com.springboot.util.JsonUtils;
import com.springboot.util.PolicyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Component
public class PolicyService implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);
    private final static int open = 0;
    private final static double resourceWeight = 1 / 3.0; /* 资源占比重 */
    private final static double testCaseWeight = 1 / 3.0; /* 测试用例占比重 */
    private final static double serviceWeight = 1 / 3.0;  /* 服务占比重 */
    private final static String baseUrl = "<a href='policy?name=%s' class='btn btn-primary btn-sm' role='button'>查看</a>";
    public static int administratorCount = 10;//默认为10
    private String[] services = {"nova", "glance", "keystone", "cinder"};
    private Map<String, API> APIMap = new HashMap<String, API>();
    private Set<String> APISet = new HashSet<String>();
    private Map<String, Double> scoreMap = new HashMap<String, Double>();
    private double threshold = 1.0;
    private List<List<String>> partitionResult = new ArrayList<List<String>>();
    private List<Policy> policyList = new ArrayList<>();
    private JsonUtils<Policy> jsonUtils;
    private File policyFile;
    private Map<String, List<Policy>> policyMap = new HashMap<>();
    private List<String> keywordList;

    @Resource
    private PolicyUtil policyUtil;

    @Resource
    private RoleService roleService;

    public List<Policy> getPolicyListByServiceName(String serviceName) {
        if (policyList == null || policyList.size() == 0) {
            fillPolicyList();
        }
        return policyList.stream().filter(policy -> policy.getSubject().equals(serviceName)).collect(Collectors.toList());
    }

    private void fillPolicyList() {
        String path = policyUtil.getName();
        policyFile = new File(path);
        jsonUtils = new JsonUtils<>();
        try {
            policyList = jsonUtils.deserialize(policyFile, Policy.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Policy> getPolicyList() {
        if (this.policyList == null || this.policyList.size() == 0) {
            fillPolicyList();
        }

        return this.policyList;
    }

    /**
     * 解析填充关键词集合
     */
    public List<String> fillKeyWordSet() {
        Map<String, Integer> keywordMap = new HashMap<>();
        if (policyList == null || policyList.size() == 0) {
            fillPolicyList();
        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        policyList.forEach(policy -> {
            String url = policy.getObject();
            Pattern pattern = Pattern.compile("^/v\\d\\.?\\d?/%UUID%/(\\w+[-?\\w+]*)/?.*$");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                fillPolicyMap(matcher, atomicInteger, keywordMap, policy);
            } else {
                Pattern pattern1 = Pattern.compile("^/v\\d\\.?\\d?/(\\w+[-?\\w+]*)/?.*$");
                Matcher matcher1 = pattern1.matcher(url);
                if (matcher1.find()) {
                    fillPolicyMap(matcher1, atomicInteger, keywordMap, policy);
                } else {
                    logger.error("{} not find keyword", url);
                }
            }
        });

        // 按关键词数量降序排序
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        keywordMap
                .entrySet()
                .stream()
                .sorted(((o1, o2) -> o2.getValue() - o1.getValue()))
                .forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
        return new ArrayList<>(sortedMap.keySet());
    }

    private void fillPolicyMap(Matcher matcher,
                               AtomicInteger atomicInteger,
                               Map<String, Integer> keywordMap,
                               Policy policy) {
        String keyword = matcher.group(1);
        atomicInteger.incrementAndGet();
        keywordMap.put(keyword, keywordMap.getOrDefault(keyword, 0) + 1);
        List<Policy> list = policyMap.getOrDefault(keyword, new ArrayList<>());
        list.add(policy);
        policyMap.put(keyword, list);
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


    /**
     * 实施结果，修改OpenStack中的policy.json
     * @newFeature: 提取关键词
     *
     * @param model
     */
    public boolean update(Model model) {
        if (administratorCount != model.getCount()) {
            administratorCount = model.getCount();
        }

        if (policyList == null || policyList.size() == 0) {
            fillPolicyList();
        }
        List<Role> roles = new ArrayList<>(administratorCount);
        IntStream.rangeClosed(1, administratorCount).forEach(i -> {
            String adminName = "admin-" + i;
            Role role = new Role();
            role.setUsername(adminName);
            role.setRole(adminName);
            role.setUrl(String.format(baseUrl, adminName));
            roles.add(role);
        });
        // TODO 修改划分权限的策略，并且提取每个分组的关键词

        int pageSize = keywordList.size() / administratorCount;
        if (pageSize < 1) {
            return false;
        }

        policyList.clear();

        int i = 1;
        int index = 0;
        while (i <= administratorCount && index < keywordList.size()) {
            String adminName = "admin-" + i;
            if (i == 1) {
                String keyword = keywordList.get(index);
                roles.get(i-1).setDuty(keyword);
                setSubject(keyword, adminName);
            } else if (i > 1 && i < administratorCount){
                String keyword1 = keywordList.get(index++);
                String keyword2 = keywordList.get(index);
                roles.get(i-1).setDuty(String.join("、", keyword1, keyword2));
                setSubject(keyword1, adminName);
                setSubject(keyword2, adminName);
            } else {
                String duty = keywordList.get(index) + "、" + keywordList.get(index + 1);
                roles.get(i-1).setDuty(duty);
                while (index < keywordList.size()) {
                    String keyword = keywordList.get(index++);
                    setSubject(keyword, adminName);
                }
            }
            i++;
            index++;
        }
        roleService.setRoleList(roles);
        // 把修改后的Policy持久化到policy.json中
        dumpPolicy();
        return true;
    }

    private void setSubject(String keyword, String adminName) {
        List<Policy> list = policyMap.get(keyword);
        list.forEach(policy -> policy.setSubject(adminName));
        policyList.addAll(list);
    }

    /**
     * 获取唯一的API
     *
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
                } else if (s.startsWith("##") && s.endsWith("##")) {
                    testCase = s.replaceAll("##", "");
                }
            }
            file.close();
        }

        logger.info("行数：" + line);

        switch (open) {
            case 0:
                logger.info("Unique API Map size: {}, Unique API Set size: {}", APIMap.size(), APISet.size());
                for (String k : APISet) {
                    logger.info(k);
                }
                break;
            case 1:
                print(APIMap);
                break;
        }
    }


    /**
     * 输出API
     *
     * @param APIMap
     */
    private void print(Map<String, API> APIMap) {
        for (Map.Entry<String, API> entry : APIMap.entrySet()) {
            logger.info(entry.getValue().toString());
        }
    }

    /**
     * 从API中获取其操作的资源
     *
     * @param api API
     * @return 资源
     */
    private String getResource(String api) {
        String[] list = api.split("\t");
        if (list.length == 2) {
            api = list[1];
            api = api.replaceAll("/%UUID%", "").replaceAll("/v\\d\\.?\\d?/", "");
            return api;
        } else {
            return "";
        }
    }

    /**
     * 处理数据
     *
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
            logger.warn("Get " + c + " Administrators! Retrying...Threshold is now: " + threshold);
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
                } else if (list1 != null && list2 != null && list1 != list2) {
                    list1.addAll(list2);
                    partitionResult.remove(list2);
                } else if (list1 != null && list2 == null) {
                    list1.add(api2);
                } else if (list1 == null) {
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
                logger.info(api);
            }
            logger.info("----------------");
            logger.info("----------------");
        }

        return partitionResult.size();
    }

    private void dumpPolicy() {
        if (policyList == null || policyList.size() == 0) {
            fillPolicyList();
        }
        try {
            jsonUtils.serialize(policyList, policyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Dump " + policyList.size() + " policy into file " + policyFile.getPath());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        keywordList = fillKeyWordSet();
    }
}
