package com.yifan.newCache;

import com.yifan.newCache.dataStructure.SkipList;
import com.yifan.newCache.model.CacheObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A huge amount of expired items are removed from container and added into container frequently,
 * SkipList is one of good choice in this use case.
 * O(logN) insert
 * O(1) to get head from bottom linkedList.
 *
 */
public class ExpiredSkipList<E extends CacheObject>{

    private final SkipList<Integer, LinkedList<E>> skipList = new SkipList<>();
    private final transient ReentrantLock lock = new ReentrantLock();

    public void put(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            LinkedList<E> list = skipList.get(e.getExpiredTime());
            if(list != null){
                list.addFirst(e);
            }else{
                skipList.put(e.getExpiredTime(), new LinkedList<E>(){{
                    addFirst(e);
                }});
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Take out all of expired item
     */
    public List<E> takeExpired() {
        final ReentrantLock lock = this.lock;
        LinkedList<E> result = new LinkedList<>();
        lock.lock();
        try {
            for (;;) {
                LinkedList<E> first = skipList.getHead();
                /**
                 * Min-heap is empty
                 */
                if (first == null)
                    return result;
                else if(first.isEmpty()){
                    skipList.removeHead();
                }
                /**
                 * Take out expired item
                 */
                else {
                    if (first.get(0).getDelay() <= 0){
                        result.addAll(first);
                        skipList.removeHead();
                    }else{
                        /**
                         * No more expired item
                         */
                        return result;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

}
