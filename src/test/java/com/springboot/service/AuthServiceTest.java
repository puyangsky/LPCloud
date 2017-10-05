package com.springboot.service;

import com.springboot.BaseTest;
import com.springboot.model.Policy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Author:      puyangsky
 * Date:        17/7/25 上午12:46
 */
public class AuthServiceTest extends BaseTest {

    @Autowired
    private AuthService service;

    @Test
    public void testEnforce() {
        assert service != null;
        assert service.enforce(new Policy("FUCK","FUCK","FUCK"));
        assert !service.enforce(new Policy("TEST","FUCK","FUCK"));
    }

    @Test
    public void test() {
        try {
            service.test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
