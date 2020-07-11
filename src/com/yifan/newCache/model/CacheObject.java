package com.yifan.newCache.model;

import java.util.concurrent.TimeUnit;

public class CacheObject<K, V>{

    private K key;
    private V value;
    private Integer priority;
    private long expiredTime;

    public CacheObject(K key, V value, Integer priority, long keepAliveTime,
                       TimeUnit unit){
        this.key = key;
        this.value = value;
        this.priority = priority;
        this.expiredTime = (System.currentTimeMillis() + unit.toMillis(keepAliveTime)) / 1000;
    }

    public CacheObject(K key, V value){
        this.key = key;
        this.value = value;
        this.expiredTime = -1L;
        this.priority = Integer.MAX_VALUE;
    }

    public long getDelay() {
        return this.expiredTime - (System.currentTimeMillis() / 1000);
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Integer getPriority() {
        return priority;
    }

    public long getExpiredTime() {
        return expiredTime;
    }
}
