package com.cgev.matcher.dto;

public class Employee {

    private String name;

    private String email;

    private String division;

    private Integer age;

    private Integer utc_offset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(Integer utc_offset) {
        this.utc_offset = utc_offset;
    }

    @Override
    public String toString() {
        return name;
    }
}