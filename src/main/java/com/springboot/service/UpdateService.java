package com.springboot.service;

import com.springboot.model.Model;
import org.springframework.stereotype.Component;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Component
public class UpdateService {
    public void update(Model model) {
        int count = model.getCount();
        double thres = model.getThreshold();
        //TODO 计算结果
        System.out.println(model.toString());


        // TODO 实施结果，修改OpenStack中的policy.json
    }
}
