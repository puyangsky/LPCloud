package com.springboot.service;

import com.springboot.model.Model;
import org.springframework.stereotype.Service;

/**
 * Author: puyangsky
 * Date: 17/5/9
 */
@Service
public class UpdateService {
    public void update(Model model) {
        int count = model.getCount();
        double thres = model.getThreshold();


    }
}
