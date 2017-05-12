package com.springboot.service;

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
    DependencyCalculateService service;

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
    }
}
