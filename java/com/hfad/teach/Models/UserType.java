package com.hfad.teach.Models;

public class UserType {
    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    private String student;
    private String teacher;
    public UserType(){

    }
}
