package com.springboot.classpath;

import com.springboot.BaseTest;
import com.springboot.util.Constants;
import org.junit.Test;

/**
 * Author:      puyangsky
 * Date:        17/6/27 上午12:34
 * Method:
 * Difficulty:
 */
public class PathTest extends BaseTest {

    @Test
    public void test() {
        String p = System.getProperty("user.home");
        System.out.println(p);
        System.out.println(Constants.RESULT_HOME);
    }
}
