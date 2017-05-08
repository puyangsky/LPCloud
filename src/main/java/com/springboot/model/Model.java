package com.springboot.model;

/**
 * Author: puyangsky
 * Date: 17/5/8
 */
public class Model {
    private int count;
    private double threshold;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "Model{" +
                "count=" + count +
                ", threshold=" + threshold +
                '}';
    }
}
