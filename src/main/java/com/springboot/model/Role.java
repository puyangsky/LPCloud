package com.springboot.model;

/**
 * Author:      puyangsky
 * Date:        17/7/24 上午12:51
 */
public class Role {
    private String role;

    private String username;

    /**
     * 主要职能，提取关键词
     */
    private String duty;

    private String url;

    public Role(String role, String username, String duty, String url) {
        this.role = role;
        this.username = username;
        this.duty = duty;
        this.url = url;
    }

    public Role() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role1 = (Role) o;

        if (role != null ? !role.equals(role1.role) : role1.role != null) return false;
        if (username != null ? !username.equals(role1.username) : role1.username != null) return false;
        if (duty != null ? !duty.equals(role1.duty) : role1.duty != null) return false;
        return url != null ? url.equals(role1.url) : role1.url == null;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (duty != null ? duty.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", duty='" + duty + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
