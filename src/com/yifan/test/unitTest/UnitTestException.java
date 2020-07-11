package com.yifan.test.unitTest;

import com.yifan.newCache.NewCache;
import com.yifan.newCache.exception.ExceedMaxSizeException;
import com.yifan.test.annotation.Test;

import java.util.concurrent.TimeUnit;

public class UnitTestException {

    @Test
    public void exceedException() {
        try{
            NewCache<String, Integer> newCache = new NewCache<>(5);
            newCache.set("A",1, 5, 100, TimeUnit.SECONDS);
            newCache.set("B",2, 15, 3, TimeUnit.SECONDS);
            newCache.set("C",3, 5, 100, TimeUnit.SECONDS);
            newCache.set("D",4, 1, 100, TimeUnit.SECONDS);
            newCache.set("E",5, 5, 150, TimeUnit.SECONDS);
            newCache.set("F",5, 5, 150, TimeUnit.SECONDS);
        }catch (ExceedMaxSizeException e){
            System.out.println("result: " + e.toString() + " correct: ExceedMaxSizeException");
        }
    }

    @Test
    public void noExpired() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        newCache.set("A", 1);
        newCache.set("B", 2);
        newCache.set("C", 3);
        newCache.set("D", 4);
        newCache.set("E", 5);

        newCache.get("A");
        newCache.setMaxItems(4);
        System.out.println("result: " + newCache.keys() + " correct: [A, C, D, E]");

        newCache.setMaxItems(2);
        System.out.println("result: " + newCache.keys() + " correct: [A, E]");
    }

    @Test
    public void coderpad() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        newCache.set("A",1);
        newCache.set("B",2);
        newCache.set("C",3);
        newCache.set("D",4);
        newCache.set("E",5);
        newCache.setMaxItems(4);
        System.out.println("result: " + newCache.keys() + " correct: [B, C, D, E]");

        newCache.setMaxItems(3);
        System.out.println("result: " + newCache.keys() + " correct: [C, D, E]");

        newCache.setMaxItems(1);
        System.out.println("result: " + newCache.keys() + " correct: [E]");

    }

}
