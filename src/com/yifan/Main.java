package com.yifan;

import com.yifan.test.annotation.Test;
import com.yifan.utils.ClassUtil;

import java.io.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

public class Main {

    public static void main(String[] args){
        try {
            Set<Class<?>> classes = ClassUtil.getClasses("com.yifan.test.unitTest");
            classes.forEach((clazz)->{
                System.out.println(clazz.getName());
                try {
                    Class<?> cls = Class.forName(clazz.getName());
                    Constructor<?> cons = cls.getConstructor();
                    Object obj = cons.newInstance();
                    Method[] methods = cls.getDeclaredMethods();
                    System.out.println("Unit Test Start: " + clazz.getName());
                    for(int i = 0; i < methods.length;i++){
                        if(methods[i].isAnnotationPresent(Test.class)){
                            System.out.println("Start method: " + methods[i].getName());
                            methods[i].invoke(obj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
