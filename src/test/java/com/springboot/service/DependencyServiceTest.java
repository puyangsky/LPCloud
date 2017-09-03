package com.springboot.service;

import com.springboot.model.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * Author: puyangsky
 * Date: 17/5/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DependencyServiceTest {

    @Autowired
    PolicyService service;

    @Test
    public void test() {
        try {
            service.getUniqueAPI();
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
        service.update(new Model(3));
    }
}
