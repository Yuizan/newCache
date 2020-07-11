package com.yifan.newCache.dataStructure;

import java.util.Random;

/**
 *
 * @param <K> priority
 * @param <V> value
 */
public class SkipList<K extends Number, V> {

    private Entry<V> head, tail, bottomHead;
    private int size;
    private int listLevel;
    private Random random;
    private double probability;
    private final double DEFAULT_PROBABILITY = 0.5;

    public SkipList(double probability){
        this.head = new Entry<>(Entry.head, null);
        this.tail = new Entry<>(Entry.tail, null);
        this.head.next = tail;
        this.tail.prev = head;
        this.size = 0;
        this.listLevel = 0;
        this.random = new Random();
        this.bottomHead = head;
        this.probability = probability;
    }

    public SkipList(){
        this.head = new Entry<>(Entry.head, null);
        this.tail = new Entry<>(Entry.tail, null);
        this.head.next = tail;
        this.tail.prev = head;
        this.size = 0;
        this.listLevel = 0;
        this.random = new Random();
        this.bottomHead = head;
        this.probability = DEFAULT_PROBABILITY;
    }

    public void put(long k, V v){
        Entry<V> findEntry = findNearestEntryByKey(k);

        if (findEntry.key == k) {
            findEntry.value = v;
            return;
        }

        Entry<V> insertEntry = new Entry<>(k, v);
        insertEntry(findEntry, insertEntry);

        if(findEntry.key == Entry.head){
            bottomHead = insertEntry;
        }

        int currentLevel = 0;
        while (random.nextDouble() > probability) {
            if (currentLevel >= listLevel) {
                addNewLevel();
            }

            /**
             * find upper node
             */
            while (findEntry.up == null) {
                findEntry = findEntry.prev;
            }

            findEntry = findEntry.up;
            Entry<V> index = new Entry<>(k, null);
            insertEntry(findEntry, index);
            index.down = insertEntry;
            insertEntry.up = index;
            /**
             * pointer go top floor
             */
            insertEntry = index;
            currentLevel++;
        }
        size++;
    }

    public V getHead(){
        if (bottomHead == null) {
            return null;
        }
        return bottomHead.value;
    }

    public V removeHead(){
        if (bottomHead == null) {
            return null;
        }
        return remove(bottomHead.key);
    }
    public V get(long k){
        Entry<V> p = findNearestEntryByKey(k);
        if (p.key == k) {
            return p.value;
        }

        return null;
    }

    public V remove(long k) {
        Entry<V> find = findNearestEntryByKey(k);
        if (find == null || find.key != k) {
            return null;
        }
        V res = find.value;

        Entry<V> temp;
        /**
         * remove all floors
         */
        while (find != null) {
            temp = find.next;
            temp.prev = find.prev;
            find.prev.next = temp;
            find = find.up;
        }
        /**
         * reset bottom head
         */
        if(this.bottomHead.key == k){
            temp = this.bottomHead.next;
            temp.prev = this.bottomHead.prev;
            this.bottomHead.prev.next = temp;
            this.bottomHead = this.bottomHead.next;
        }
        return res;
    }

    private void addNewLevel() {
        Entry<V> newHead = new Entry<>(Entry.head, null);
        Entry<V> newEnd = new Entry<>(Entry.tail, null);
        newHead.next = newEnd;
        newHead.down = this.head;
        newEnd.prev = newHead;
        newEnd.down = this.tail;
        this.head.up = newHead;
        this.tail.up = newEnd;
        this.head = newHead;
        this.tail = newEnd;
        this.listLevel++;
    }

    private Entry<V> findNearestEntryByKey(long k){
        Entry<V> entry = head;
        for(;;){
            /**
             * go right
             */
            for (Entry current = entry; current != null; current = current.next) {
                if(current.key > k){
                    break;
                }
                entry = current;
            }

            /**
             * go down
             */
            if(entry.down != null) {
                entry = entry.down;
            }else{
                return entry;
            }
        }
    }

    private void insertEntry(Entry<V> prev, Entry<V> node) {
        node.next = prev.next;
        node.prev = prev;
        prev.next.prev = node;
        prev.next = node;
    }

    public int size(){
        return this.size;
    }

    private void printVertical() {
        String s = "";
        Entry p;
        p = head;
        while ( p.down != null )
            p = p.down;
        while ( p != null ) {
            s = getOneColumn( p );
            System.out.println(s);
            p = p.next;
        }
    }
    private String getOneColumn( Entry p ) {
        String s = "";
        while ( p != null ) {
            s = s + " " + p.key;
            p = p.up;
        }
        return(s);
    }

    class Entry<T> {
        public long key;
        public T value;
        public Entry<T> prev, next, up, down;

        public static final long head = Long.MIN_VALUE;
        public static final long tail = Long.MAX_VALUE;

        public Entry() {}

        public Entry(long k, T v) {
            key = k;
            value = v;
        }
    }


    public static void main(String[] args) {
        SkipList<Integer, String> skipList = new SkipList<>();
        skipList.put(4, "d");
        skipList.put(1, "a");
        skipList.put(2, "b");
        skipList.put(5, "e");
        skipList.put(3, "c");
        skipList.put(8, "d");
        skipList.put(9, "a");
        skipList.put(10, "b");
        skipList.put(11, "e");
        skipList.put(12, "c");

        skipList.removeHead();
        skipList.printVertical();
        System.out.println(skipList.getHead());
        skipList.removeHead();
        skipList.printVertical();
        System.out.println(skipList.getHead());
        skipList.removeHead();
        skipList.printVertical();
        System.out.println(skipList.getHead());
    }
}
