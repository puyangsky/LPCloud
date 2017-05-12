package com.springboot.model;

/**
 * Author: puyangsky
 * Date: 17/5/12
 */
public class API {
    private String service;
    private String body;
    private String resource;
    private String testCase;

    public API(String service, String body, String resource, String testCase) {
        this.service = service;
        this.body = body;
        this.resource = resource;
        this.testCase = testCase;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "API{" +
                "service='" + service + '\'' +
                ", body='" + body + '\'' +
                ", resource='" + resource + '\'' +
                ", testCase='" + testCase + '\'' +
                '}';
    }
}
