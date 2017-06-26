package com.springboot.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: puyangsky
 * Date: 17/5/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisUtil redis;

    @Test
    public void test() {
        redis.set("a", "b");
        String a = redis.get("a");
        System.out.println(a);
    }
}