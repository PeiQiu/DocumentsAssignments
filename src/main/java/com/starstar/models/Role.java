package com.starstar.models;

/**
 * Created by starstar on 17/4/27.
 */
public class Role {
    public Role(){

    }
    public Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private String authority;


}
