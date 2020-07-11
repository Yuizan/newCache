package com.yifan.newCache;

import com.yifan.newCache.dataStructure.LRUCache;
import com.yifan.newCache.dataStructure.SortedZipList;
import com.yifan.newCache.model.CacheObject;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PriorityHeap
 *
 * Priority would not much in most cases,
 * Use zipList instead of skipList or min-heap would save more physical memories,
 * @param <E>
 */
public class PriorityZSet<E extends CacheObject>{

    /**
     * Min-heap of PriorityLinkedItem (Priority and LRUCAche)
     */
    private SortedZipList<LRUCache<Object, E>> list;
    /**
     * Map of priority and lruCache, spend a bit more space to get faster query performance O(1)
     */
    private final transient ReentrantLock lock = new ReentrantLock();

    PriorityZSet(){
        this.list = new SortedZipList<>();
    }

    public E get(E e){

        LRUCache<Object, E> lruCache = list.get(e.getPriority());
        return lruCache.get(e.getKey());
    }

    public void put(E e){
        final ReentrantLock lock = this.lock;
        LRUCache<Object, E> lruCache = list.get(e.getPriority());
        /**
         * Double-Checked Locking,
         * if there is lruCache in indexMap,
         * it will be not necessary to lock the current thread
         */
        if(lruCache == null){
            try{
                lock.lock();
                /**
                 * Double-Checked lruCache,
                 * otherwise, many of threads would go into it and create duplicated lru caches in concurrent cases
                 */
                lruCache = list.get(e.getPriority());
                if(lruCache == null){
                    lruCache = new LRUCache<>();
                    this.list.add(e.getPriority(), lruCache);
                }
            }finally {
                lock.unlock();
            }
        }
        lruCache.put(e.getKey(), e);
    }

    /**
     * remove the lowest priority item,
     * then remove lru item
     */
    public E remove(){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            /**
             * if the size of lru cache in lower priority is 0,
             * it will remove the lru cache and try to get higher priority lru cache
             */
            for(;;){
                /**
                 * there is empty in priority min-heap
                 */
                LRUCache<Object, E> first = this.list.getFirst();
                if (first == null)
                    return null;


                E cacheObject = first.getHead();
                if(cacheObject != null){
                    /**
                     * remove the item in lru cache
                     */
                    first.remove(cacheObject.getKey());
                    return cacheObject;
                }
                /**
                 * Current LRU is empty, remove it and try to find next;
                 */
                this.list.removeFirst();
            }
        } finally {
            lock.unlock();
        }
    }

    public E remove(E e){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            LRUCache<Object, E> lruCache = this.list.get(e.getPriority());
            return lruCache.remove(e.getKey());
        } finally {
            lock.unlock();
        }
    }



}



