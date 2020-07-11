package com.yifan.newCache.dataStructure;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

    private Map<K, DoublyLinkedList.ListNode<V>> indexMap = new HashMap<>();
    private DoublyLinkedList<V> list = new DoublyLinkedList<>();

    public LRUCache(){}

    public V getHead(){
        DoublyLinkedList.ListNode<V> head = list.getHead();
        if(head == null){
            return null;
        }
        return list.getHead().value;
    }

    public V get(K key) {
        DoublyLinkedList.ListNode<V> node = indexMap.get(key);
        if(node == null){
            return null;
        }
        list.remove(node);
        list.add(node);
        return node.value;
    }

    public synchronized V put(K key, V value) {
        DoublyLinkedList.ListNode<V> node = new DoublyLinkedList.ListNode<>(value);
        indexMap.put(key, node);
        list.add(node);
        return node.value;
    }

    public synchronized V remove(K key) {
        DoublyLinkedList.ListNode<V> node = indexMap.get(key);
        if(node == null){
            return null;
        }
        indexMap.remove(key);
        list.remove(node);
        return node.value;
    }

    public static void main(String[] args) {
        LRUCache<String, String> lruCache = new LRUCache<>();
        lruCache.put("a","a");
        lruCache.put("b","b");
        lruCache.put("c","c");
        lruCache.get("b");
        System.out.println(lruCache.list.toString());
    }
}
