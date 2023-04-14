package com.example.spring.dto;

import com.example.spring.models.Enterprise;

import java.io.Serializable;

public class EnterpriseDTO implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String phone;

    public EnterpriseDTO() {
    }

    public EnterpriseDTO(Long id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public EnterpriseDTO(Enterprise enterprise) {
        this.id = enterprise.getId();
        this.name = enterprise.getName();
        this.address = enterprise.getAddress();
        this.phone = enterprise.getPhone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
