package com.yifan.test.unitTest;

import com.yifan.newCache.NewCache;
import com.yifan.test.annotation.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UnitTestConcurrent {

    @Test
    public void coderpad() {
        NewCache<String, Integer> newCache = new NewCache<>(5);
        try {
            newCache.set("A",1, 5, 100, TimeUnit.SECONDS);
            CountDownLatch countDownLatch = new CountDownLatch(4);
            add(newCache, countDownLatch, "B",2, 15, 3, TimeUnit.SECONDS);
            add(newCache, countDownLatch, "C",3, 5, 1000, TimeUnit.SECONDS);
            add(newCache, countDownLatch, "D",4, 1, 1000, TimeUnit.SECONDS);
            add(newCache, countDownLatch, "E",5, 5, 1500, TimeUnit.SECONDS);
            countDownLatch.await();
            newCache.get("C");
            Thread.sleep(6000);
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

    private void add(NewCache<String, Integer> newCache, CountDownLatch countDownLatch, String key, Integer value, Integer priority, long keepAliveTime,
                     TimeUnit unit){
        new Thread(()->{
            newCache.set(key, value, priority, keepAliveTime, unit);
            countDownLatch.countDown();
        }).start();
    }
}
