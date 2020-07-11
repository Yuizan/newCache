package com.yifan.newCache.dataStructure;


public class DoublyLinkedList<T>{

    private ListNode<T> head;
    private ListNode<T> tail;
    private int size = 0;

    public DoublyLinkedList(){
        head = null;
        tail = null;
    }

    /**
     * Add to tail
     * @param node element
     */
    public void add(ListNode<T> node) {
        if(node == null)
            throw new NullPointerException();

        node.prev = null;
        node.next = null;
        if(this.head == null){
            this.head = node;
            this.tail = this.head;
        }else{
            ListNode<T> temp = this.tail;
            this.tail.next = node;
            this.tail = node;
            this.tail.prev = temp;
        }
        this.size++;
    }

    public void remove(ListNode<T> node) {

        if(node == null)
            throw new NullPointerException();


        if(this.size == 1){
            this.head = null;
            this.tail = null;
        }else if(node == head){
            head = head.next;
            head.prev = null;
        }else if(node == tail){
            tail = tail.prev;
            tail.next = null;
        }else{
            node.prev.next = node.next;
        }

        this.size--;
    }

    public ListNode<T> getHead(){
        return this.head;
    }

    public ListNode<T> getTail(){
        return this.tail;
    }

    public String toString() {

        if (this.size == 0) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder("[");
            for (ListNode current = head; current != null; current = current.next) {
                sb.append(current.value.toString() + ", ");
            }
            int len = sb.length();
            return sb.delete(len - 2, len).append("]").toString();
        }
    }


    static class ListNode<T>{
        public T value;
        public ListNode prev;
        public ListNode next;
        public ListNode(){}

        public ListNode(T value){
            this.value = value;
            prev = null;
            next = null;
        }
    }

    public static void main(String[] args) {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        ListNode<String> node = new ListNode<>("d");
        list.add(new ListNode<>("a"));
        list.add(new ListNode<>("b"));
        list.add(new ListNode<>("c"));
        list.add(node);

        list.remove(node);
        System.out.println(list.toString());
        System.out.println(list.getHead().value);
        System.out.println(list.getTail().value);
    }
}
