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

        if (subject != null ? !subject.equals(policy.subject) : policy.subject != null) return false;
        if (object != null ? !object.equals(policy.object) : policy.object != null) return false;
        return action != null ? action.equals(policy.action) : policy.action == null;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
