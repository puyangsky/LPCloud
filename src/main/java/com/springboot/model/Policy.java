package com.springboot.model;

/**
 * Author:      puyangsky
 * Date:        17/7/24 上午12:51
 * Method:
 * Difficulty:
 */
public class Policy {
    private String subject;
    private String object;
    private String action;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
