package com.example.spring.dto;

import com.example.spring.models.Employee;

import java.io.Serializable;

public class EmployeeDTO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String position;
    private String email;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String firstName, String lastName, Integer age, String position, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.position = position;
        this.email = email;
    }

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.age = employee.getAge();
        this.position = employee.getPosition();
        this.email = employee.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
