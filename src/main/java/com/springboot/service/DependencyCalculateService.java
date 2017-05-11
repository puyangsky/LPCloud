package com.springboot.service;

import com.springboot.model.Model;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Component
public class DependencyCalculateService {
    public void update(Model model) {
        int count = model.getCount();
        double thres = model.getThreshold();
        //TODO 计算结果
        System.out.println(model.toString());
        // TODO 实施结果，修改OpenStack中的policy.json

        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() throws IOException {

//        String path = this.getClass().getResource("/").getPath() + "static/sourceFile/cinder.log";
//        System.out.println(path);

//        File f3 = new File(this.getClass().getResource("").getPath());
//        System.out.println(f3);

        File f4 = ResourceUtils.getFile("classpath:static/sourceFile/cinder.log");
        System.out.println(f4.getPath());

        RandomAccessFile file = new RandomAccessFile(f4, "r");
        String s;
        while ((s = file.readLine()) != null) {
            System.out.println(s);
        }
    }

}
