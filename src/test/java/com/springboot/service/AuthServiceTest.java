package com.springboot.service;

import com.springboot.BaseTest;
import com.springboot.model.Policy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author:      puyangsky
 * Date:        17/7/25 上午12:46
 * Method:
 * Difficulty:
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
}
