package com.example.mystorycanvas;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestClass {
    private List<Object> list=new ArrayList<>();

    ChildClass childClass=new ChildClass();
    public void hello()
    {
        childClass.setRollno(10);
        childClass.setName("Hello");
        list.add(childClass);
        ParentClass parentClass=new ParentClass();
        parentClass.setName("parent");
        for(Object obj:list) {
            if(obj.getClass()==ChildClass.class) {
                ChildClass obj2 = (ChildClass) obj;
                int roll=obj2.getRollno();
                obj2.getName();
                System.out.println(roll);
            }
            else {
                ParentClass obj1 = (ParentClass) obj;
                obj1.getName();
            }
        }
    }
}
