package com.springboot.classpath;

import com.springboot.BaseTest;
import com.springboot.service.AuthService;
import com.springboot.util.Constants;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Author:      puyangsky
 * Date:        17/6/27 上午12:34
 * Method:
 * Difficulty:
 */
public class PathTest {

    @Test
    public void test() {
        String p = System.getProperty("user.home");
        System.out.println(p);
        System.out.println(Constants.RESULT_HOME);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println(loader.getResource("").getPath());
        System.out.println(this.getClass().getResource("").getPath());
        System.out.println(this.getClass().getResource("/").getPath());
        System.out.println(System.getProperty("user.dir"));
    }


    @Test
    public void beanTest() throws IOException {
        ClassPathResource resource = new ClassPathResource("bean.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
        Object bean = factory.getBean("authService");
        factory.containsBean("authService");
        assert bean != null;

        AuthService service = (AuthService) bean;
        try {
            service.test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t1 working");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("t1 begin");

        t1.start();

        t1.join();


        System.out.println("t1 end");
    }


    @Test
    public void testWaitNotify() throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Object lock3 = new Object();
        MyThread t1 = new MyThread(lock1, lock2, "A");
        MyThread t2 = new MyThread(lock2, lock3, "B");
        MyThread t3 = new MyThread(lock3, lock1, "C");

        t1.start();
        t2.start();
        t3.start();
        synchronized (lock3) {
            lock3.notify();
        }

        t1.join();
        t2.join();
        t3.join();
    }

    class MyThread extends Thread {
        private Object lock1;
        private Object lock2;
        private String name;
        public MyThread(Object lock1, Object lock2, String name) {
            this.lock1 = lock1;
            this.lock2 = lock2;
            this.name = name;
        }

        @Override
        public void run() {
            super.run();
            synchronized (this.lock1) {
                try {
                    lock1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this.name);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock2.notify();
            }
        }
    }
}
