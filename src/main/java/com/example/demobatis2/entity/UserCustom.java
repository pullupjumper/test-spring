package com.example.demobatis2.entity;

import java.util.List;

public class UserCustom extends User {

    private List<Role>roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
