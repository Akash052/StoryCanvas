package com.example.mystorycanvas;

public class ChildClass extends ParentClass{
    private int rollno;

    public int getRollno() {
        return rollno;
    }

    public void setRollno(int rollno) {
        this.rollno = rollno;
        setName("Child class name");
    }
}
