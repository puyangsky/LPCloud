package com.springboot.service;

import com.springboot.BaseTest;
import com.springboot.model.Model;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * Author: puyangsky
 * Date: 17/5/12
 */
public class DependencyServiceTest extends BaseTest{

    @Autowired
    private PolicyService policyService;

    @Test
    public void testFillKeyWordSet() {
        List<String> keyWordList = policyService.fillKeyWordSet();
        System.out.println(keyWordList.size());
    }

    @Test
    public void test() {
        try {
            policyService.getUniqueAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws FileNotFoundException {

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/imac/Desktop/test.txt", true)));
            out.write("fuck");
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


    @Test
    public void reTest() {
        String s = "/v2.1/servers";
        s = s.replaceAll("/v\\d\\.?\\d?/", "");
        System.out.println(s);


        String s3 = "s";

        String s4 = "s";

        System.out.println(s3==s4);


        Integer i = 2017;

        Integer j = 2017;

        System.out.println(i==j);


        String s1 = new String("s");

        String s2 = new String("s");

        System.out.println(s1.intern()==s2.intern());
        System.out.println(s1==s2);

        String s5 = "hel" + "lo";
        String s6 = "hello";
        System.out.println(s5 == s6);
    }


    @Test
    public void test2() {
        policyService.update(new Model(10));
    }
}
