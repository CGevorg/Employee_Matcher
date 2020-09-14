package com.cgev.matcher.dto;

public class Employee {

    private String name;

    private String email;

    private String division;

    private Integer age;

    private Integer utcOffset;

    private String location;

    private String sameLocationPreference;

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

    @Override
    public String toString() {
        return name;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSameLocationPreference() {
        return sameLocationPreference;
    }

    public void setSameLocationPreference(String sameLocationPreference) {
        this.sameLocationPreference = sameLocationPreference;
    }
}