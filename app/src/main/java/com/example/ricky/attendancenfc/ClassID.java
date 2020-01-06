package com.example.ricky.attendancenfc;

import java.util.ArrayList;

public class ClassID {
    private String subjectName;

    public ClassID(String subjectName, String classNumber) {
        this.subjectName = subjectName;
        this.classNumber = classNumber;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    private String classNumber;

    public static ClassID search(ArrayList<ClassID> classList,String classNo) {
        for (ClassID classID : classList) {
            if (classID.getClassNumber().equals(classNo)) {
                return classID;
            }
        }
        ClassID classID = new ClassID("","");
        return classID;
    }
}
