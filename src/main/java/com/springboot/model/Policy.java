package com.springboot.model;

/**
 * Author:      puyangsky
 * Date:        17/7/24 上午12:51
 * Method:
 * Difficulty:
 */
public class Policy {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Policy(int id, String subject, String object, String action) {
        this.id = id;
        this.subject = subject;
        this.object = object;
        this.action = action;
    }

    public Policy(String subject, String object, String action) {
        this.subject = subject;
        this.object = object;
        this.action = action;
    }

    public Policy() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Policy policy = (Policy) o;

        if (id != policy.id) return false;
        if (!subject.equals(policy.subject)) return false;
        if (!object.equals(policy.object)) return false;
        return action.equals(policy.action);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + subject.hashCode();
        result = 31 * result + object.hashCode();
        result = 31 * result + action.hashCode();
        return result;
    }
}
