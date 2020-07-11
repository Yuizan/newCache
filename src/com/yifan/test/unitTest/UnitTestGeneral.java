package com.yifan.test.unitTest;

import com.yifan.newCache.NewCache;
import com.yifan.test.annotation.Test;

import java.util.concurrent.TimeUnit;

public class UnitTestGeneral {

    @Test
    public void coderpad() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        try {
            newCache.set("A",1, 5, 100, TimeUnit.SECONDS);
            newCache.set("B",2, 15, 3, TimeUnit.SECONDS);
            newCache.set("C",3, 5, 100, TimeUnit.SECONDS);
            newCache.set("D",4, 1, 100, TimeUnit.SECONDS);
            newCache.set("E",5, 5, 150, TimeUnit.SECONDS);
            newCache.get("C");
            Thread.sleep(5000);
            newCache.setMaxItems(4);
            System.out.println("result: " + newCache.keys() + " correct: [A, C, D, E]");

            newCache.setMaxItems(3);
            System.out.println("result: " + newCache.keys() + " correct: [A, C, E]");

            newCache.setMaxItems(2);
            System.out.println("result: " + newCache.keys() + " correct: [C, E]");

            newCache.setMaxItems(1);
            System.out.println("result: " + newCache.keys() + " correct: [C]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void expired() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        try {
            newCache.set("A",1, 5, 1, TimeUnit.SECONDS);
            newCache.set("B",2, 15, 1, TimeUnit.SECONDS);
            newCache.set("C",3, 5, 1, TimeUnit.SECONDS);
            newCache.set("D",4, 1, 1, TimeUnit.SECONDS);
            newCache.set("E",5, 5, 2, TimeUnit.SECONDS);
            Thread.sleep(1000);
            newCache.setMaxItems(4);
            System.out.println("result: " + newCache.keys() + " correct: [E]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void priority() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        newCache.set("A",1, 1, 1000, TimeUnit.SECONDS);
        newCache.set("B",2, 2, 1000, TimeUnit.SECONDS);
        newCache.set("C",3, 3, 1000, TimeUnit.SECONDS);
        newCache.set("D",4, 4, 1000, TimeUnit.SECONDS);
        newCache.set("E",5, 5, 1000, TimeUnit.SECONDS);
        newCache.setMaxItems(4);
        System.out.println("result: " + newCache.keys() + " correct: [B, C, D, E]");
        newCache.setMaxItems(3);
        System.out.println("result: " + newCache.keys() + " correct: [C, D, E]");
        newCache.setMaxItems(1);
        System.out.println("result: " + newCache.keys() + " correct: [E]");
    }

    @Test
    public void lru() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        newCache.set("A",1, 15, 10, TimeUnit.SECONDS);
        newCache.set("B",2, 15, 10, TimeUnit.SECONDS);
        newCache.set("C",3, 15, 10, TimeUnit.SECONDS);
        newCache.set("D",4, 15, 10, TimeUnit.SECONDS);
        newCache.set("E",5, 15, 10, TimeUnit.SECONDS);
        newCache.get("A");
        newCache.setMaxItems(4);
        System.out.println("result: " + newCache.keys() + " correct: [A, C, D, E]");
        newCache.setMaxItems(3);
        System.out.println("result: " + newCache.keys() + " correct: [A, D, E]");
        newCache.get("D");
        newCache.get("E");
        newCache.setMaxItems(2);
        System.out.println("result: " + newCache.keys() + " correct: [D, E]");
        newCache.get("D");
        newCache.setMaxItems(1);
        System.out.println("result: " + newCache.keys() + " correct: [D]");
    }

    @Test
    public void noMaxSize() {
        NewCache<String, Integer> newCache = new NewCache<>();
        try {
            newCache.set("A",1, 5, 100, TimeUnit.SECONDS);
            newCache.set("B",2, 15, 3, TimeUnit.SECONDS);
            newCache.set("C",3, 5, 100, TimeUnit.SECONDS);
            newCache.set("D",4, 1, 100, TimeUnit.SECONDS);
            newCache.set("E",5, 5, 150, TimeUnit.SECONDS);
            newCache.get("C");
            Thread.sleep(5000);
            newCache.setMaxItems(4);
            System.out.println("result: " + newCache.keys() + " correct: [A, C, D, E]");

            newCache.setMaxItems(3);
            System.out.println("result: " + newCache.keys() + " correct: [A, C, E]");

            newCache.setMaxItems(2);
            System.out.println("result: " + newCache.keys() + " correct: [C, E]");

            newCache.setMaxItems(1);
            System.out.println("result: " + newCache.keys() + " correct: [C]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void normalUse() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        try {
            newCache.set("A",1, 5, 100, TimeUnit.SECONDS);
            newCache.set("B",2, 15, 1, TimeUnit.SECONDS);
            Thread.sleep(1000);
            System.out.println("result: " + newCache.keys() + " correct: [A]");

            newCache.set("B",1, 10, 100, TimeUnit.SECONDS);
            System.out.println("result: " + newCache.keys() + " correct: [A, B]");

            newCache.set("B",1, 10, 100, TimeUnit.SECONDS);
            System.out.println("result: " + newCache.keys() + " correct: [A, B]");

            newCache.set("C",1, 7, 100, TimeUnit.SECONDS);
            newCache.set("D",1, 7, 100, TimeUnit.SECONDS);
            newCache.set("E",1, 7, 100, TimeUnit.SECONDS);
            newCache.setMaxItems(4);
            System.out.println("result: " + newCache.keys() + " correct: [B, C, D, E]");

            newCache.get("D");
            newCache.setMaxItems(3);
            System.out.println("result: " + newCache.keys() + " correct: [B, D, E]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void expiredTwo() throws InterruptedException {
        NewCache<Integer, Integer> newCache = new NewCache<>();
        newCache.set(1, 1, 10, 1L, TimeUnit.SECONDS);
        newCache.set(0, 0, 10, 1L, TimeUnit.SECONDS);
        Thread.sleep(1000);
        newCache.set(1, 1, 10, 10000L, TimeUnit.SECONDS);
        System.out.println("result: " + newCache.keys() + " correct: [1]");

    }

}
