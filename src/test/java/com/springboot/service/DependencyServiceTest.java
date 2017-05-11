package com.springboot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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
            service.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
