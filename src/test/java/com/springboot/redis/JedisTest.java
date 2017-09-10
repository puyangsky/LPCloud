package com.springboot.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Author:      puyangsky
 * Date:        17/9/5 下午9:31
 */
public class JedisTest {

    private Jedis jedisCli = new Jedis("127.0.0.1",6379);

    @Test
    public void test() {
        System.out.println(jedisCli.clusterInfo());
    }


    @Test
    public void transactionTest() {
        Transaction tran = jedisCli.multi();

        tran.set("a", "tran");

        tran.get("a");

        List<Object> res = tran.exec();

        if (res == null) {
            System.out.println("error");
        }else {
            res.forEach(System.out::println);
        }
    }
}
