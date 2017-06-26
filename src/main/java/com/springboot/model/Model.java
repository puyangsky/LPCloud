package com.springboot.model;

/**
 * Author: puyangsky
 * Date: 17/5/8
 */
public class Model {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Model(int count) {
        this.count = count;
    }

    public Model() {
    }

    @Override
    public String toString() {
        return "Model{" +
                "count=" + count +
                '}';
    }
}
