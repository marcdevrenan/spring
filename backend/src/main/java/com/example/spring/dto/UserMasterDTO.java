package com.example.spring.dto;

public class UserMasterDTO extends UserDTO {

    private String password;

    public UserMasterDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
