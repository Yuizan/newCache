package com.yifan.newCache;

import com.yifan.newCache.exception.ExceedMaxSizeException;
import com.yifan.newCache.model.CacheObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Your task is to implement a PriorityExpiryCache cache with a max capacity
 * @param <K>
 * @param <V>
 */
public class NewCache<K, V> {

    private Map<K, CacheObject<K, V>> cacheObjectMap;
    private volatile transient ExpiredSkipList<CacheObject<K, V>> expiredSkipList;
    private volatile transient PriorityZSet<CacheObject<K, V>> PriorityZSet;

    /** Max size of new cache, -1 means infinity */
    private volatile transient int maxSize;

    /**
     * Creates a new, empty map with the default initial table size (16).
     */
    public NewCache(){
        cacheObjectMap = new ConcurrentHashMap<>();
        expiredSkipList = new ExpiredSkipList<>();
        PriorityZSet = new PriorityZSet<>();
        maxSize = -1;
    }

    /**
     * Creates a new, empty map with the suitable initial table size (2 square).
     * For Example maxSize: 23 < 32 * 0.75(LOAD_FACTOR)
     * capacity = 1 << 5 ( 32 )
     */
    public NewCache(int maxSize){
        if(maxSize > 0){
            /** Find the suitable map size to reduce the resize operation would run */
            int capacity = 1 << 4;
            while(capacity * 0.75f < maxSize){
                capacity = capacity << 1;
            }
            cacheObjectMap = new ConcurrentHashMap<>(capacity);
            expiredSkipList = new ExpiredSkipList<>();
            PriorityZSet = new PriorityZSet<>();
            this.maxSize = maxSize;
        }else{
            throw new IllegalArgumentException("Illegal Capacity: "+ maxSize);
        }

    }

    /**
     * put item into map with priority and keepAliveTime
     */
    public void set(K key, V value, Integer priority, long keepAliveTime,
                    TimeUnit unit){
        CacheObject<K, V> cacheObject = new CacheObject<>(key, value, priority, keepAliveTime, unit);
        cacheObjectMap.put(key, cacheObject);
        if(maxSize >= 0 && cacheObjectMap.size() > maxSize)
            throw new ExceedMaxSizeException("Max Size:" + maxSize + ", current size:"+ cacheObjectMap.size());
        expiredSkipList.put(cacheObject);
        PriorityZSet.put(cacheObject);


    }

    /**
     * put item into map
     */
    public void set(K key, V value){
        CacheObject<K, V> cacheObject = new CacheObject<>(key, value);
        cacheObjectMap.put(key, cacheObject);
        if(maxSize >= 0 && cacheObjectMap.size() > maxSize)
            throw new ExceedMaxSizeException("Max Size:" + maxSize + ", current size:"+ cacheObjectMap.size());
        PriorityZSet.put(cacheObject);
    }


    /**
     * get item from map
     */
    public V get(K key){
        CacheObject<K, V> cacheObject = cacheObjectMap.get(key);
        if(cacheObject == null){
            return null;
        }

        /** refresh item where it in the lru cache  */
        PriorityZSet.get(cacheObject);


        /** double check expired time*/
        if(cacheObject.getDelay() < 0){
            return null;
        }

        return cacheObject.getValue();
    }

    public void setMaxItems(int maxSize){
        this.maxSize = maxSize;
        evictItems();
    }


    public Set<K> keys(){
        this.evictItems();
        return cacheObjectMap.keySet();
    }


    /**
     * Remove expired,
     * lowest priority,
     * least recently used items
     * */
    private void evictItems() {
        /**
         * Remove expired,
         * */
        List<CacheObject<K, V>> expiredObjects = expiredSkipList.takeExpired();
        for(CacheObject<K, V> cacheObject: expiredObjects){
            if(cacheObjectMap.get(cacheObject.getKey()) == cacheObject){
                cacheObjectMap.remove(cacheObject.getKey());
                PriorityZSet.remove(cacheObject);
            }
        }

        if(this.maxSize < 0){
            return;
        }
        /**
         * lowest priority,
         * least recently used items
         * */
        if(cacheObjectMap.size() - this.maxSize <= 0)
            return;

        while(cacheObjectMap.size() - this.maxSize > 0){
            CacheObject<K, V> cacheObject = PriorityZSet.remove();
            if(null != cacheObject
                    && cacheObject == cacheObjectMap.get(cacheObject.getKey())
            ){
                cacheObjectMap.remove(cacheObject.getKey());
            }
        }
    }

    public int getSize(){
        return cacheObjectMap.size();
    }
}
