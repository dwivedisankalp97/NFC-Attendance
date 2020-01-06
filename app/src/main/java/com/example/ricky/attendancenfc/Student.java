package com.example.ricky.attendancenfc;

public class Student {
    String name;
    String regNo;
    boolean status;
    String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    Student(String name, String regNo, boolean status,String Tag)
    {
        this.name = name;
        this.regNo = regNo;
        this.status = status;
        this.tag = Tag;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}
