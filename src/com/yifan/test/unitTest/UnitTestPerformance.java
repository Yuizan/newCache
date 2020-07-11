package com.yifan.test.unitTest;

import com.yifan.newCache.NewCache;
import com.yifan.test.annotation.Test;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

public class UnitTestPerformance {


    @Test
    public void performace() throws InterruptedException {
        NewCache<Integer, Integer> newCache = new NewCache<>();

        int count = 10, coreGenerateNum = 300000;
        ExecutorService executorService = new ThreadPoolExecutor(count, count, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());


        CountDownLatch countDownLatch = new CountDownLatch(count);
        long start = System.currentTimeMillis();
        for(int i = 0; i < count; i++){
            int finalI = i;
            executorService.execute(()->{
                for(int j = 0; j < coreGenerateNum;j++){
                    Integer num = count * finalI * coreGenerateNum + j;
                    newCache.set(num, num);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("size:" + newCache.getSize() + " add operation total spent: " + (end - start) + "ms");
        executorService.shutdown();

        long getStart = System.currentTimeMillis();
        newCache.get(1);
        long getEnd = System.currentTimeMillis();
        System.out.println("get value from cache, total size:" + newCache.getSize() + " total spent: " + (getEnd - getStart) + "ms");
        long removeStart = System.currentTimeMillis();
        newCache.setMaxItems(1);
        long removeEnd = System.currentTimeMillis();
        System.out.println("size:" + newCache.getSize() + " setMaxItems total spent: " + (removeEnd - removeStart) + "ms");
    }

    @Test
    public void expire() throws InterruptedException {
        NewCache<Integer, Integer> newCache = new NewCache<>();
        int count = 10, coreGenerateNum = 30000;
        ExecutorService executorService = new ThreadPoolExecutor(count, count, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());


        CountDownLatch countDownLatch = new CountDownLatch(count);
        for(int i = 0; i < count; i++){
            int finalI = i;
            executorService.execute(()->{
                for(int j = 0; j < coreGenerateNum;j++){
                    Integer num = count * finalI * coreGenerateNum + j;
                    newCache.set(num, num, 10, 1L, TimeUnit.SECONDS);
                }
                countDownLatch.countDown();
            });
        }
        newCache.set(1, 1, 10, 1L, TimeUnit.SECONDS);
        newCache.set(0, 0, 10, 1L, TimeUnit.SECONDS);
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("current size: " + newCache.getSize());
        Thread.sleep(1000);

        newCache.set(1, 1, 10, 10000L, TimeUnit.SECONDS);
        long removeStart = System.currentTimeMillis();
        newCache.setMaxItems(1);
        long removeEnd = System.currentTimeMillis();
        System.out.println("size:" + newCache.getSize() + " setMaxItems total spent: " + (removeEnd - removeStart) + "ms");
        System.out.println(newCache.keys());

    }

}
