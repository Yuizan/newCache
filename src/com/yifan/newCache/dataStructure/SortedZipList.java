package com.yifan.newCache.dataStructure;

public class SortedZipList<T> {

    private Object[] data;
    private static final int startOffset = 3;

    public SortedZipList(){
        this.data = new Object[1 << 4];
        this.data[0] = 1 << 4; // total size
        this.data[1] = 2; // tail offset
        this.data[2] = 2; // item length;    0: data   1: score
    }

    public void add(int score, T t){
        // when data is empty
        if(this.getTailOffset() == startOffset - 1){
            this.data[this.getTailOffset() + 1] = t;
            this.data[this.getTailOffset() + 2] = score;
        }
        //  should add to head
        else if(score < (int)this.data[startOffset + 1]){
            int tail = this.getTailOffset();
            System.arraycopy(this.data, startOffset, this.data, startOffset + 2, tail + 1 - startOffset);
            this.data[startOffset] = t;
            this.data[startOffset + 1] = score;
        }
        // should add to tail
        else if(score > (int)this.data[this.getTailOffset()]){
            this.data[this.getTailOffset() + 1] = t;
            this.data[this.getTailOffset() + 2] = score;
        }
        // find
        else{
            int offset = this.findInsertOffset(score);
            if((int)this.data[offset + 1] == score){
                this.data[offset] = t;
                return;
            }

            int tail = this.getTailOffset();
            System.arraycopy(this.data, offset, this.data, offset + 2, tail + 1 - offset);
            this.data[offset] = t;
            this.data[offset + 1] = score;

        }

        setTailOffset(this.getTailOffset() + this.getItemLength());
        if((this.getTailOffset() + this.getItemLength()) > (this.getTotalSize() - 1))
            this.resize();
    }

    public T getFirst(){
        return (T) this.data[startOffset];
    }

    public T get(int score){
       int offset = this.getOffset(score);
       if(offset < 0){
           return null;
       }
       return (T) this.data[startOffset + offset * this.getItemLength()];
    }

    public T removeFirst(){
        if(this.data[startOffset + 1] == null){
            return null;
        }
        return (T) this.remove((int)this.data[startOffset + 1]);
    }

    public T remove(int score){
        int offset = this.getOffset(score);
        if(offset< 0){
            return null;
        }
        T res = (T) this.data[(startOffset - 1 ) + offset * this.getItemLength() + 1];
        System.arraycopy(this.data, (startOffset - 1) + (offset + 1) * this.getItemLength() + 1,
                this.data, (startOffset - 1) + offset * this.getItemLength() + 1,
                this.getTailOffset() - (startOffset - 1 + offset * this.getItemLength()));
        this.setTailOffset(this.getTailOffset() - 2);
        return res;
    }

    private int getOffset(int score){
        int min = 0;
        int max = (this.getTailOffset() + 1 - startOffset) / this.getItemLength() - 1;
        while(min <= max){
            int average = (max + min) / 2;
            int compare = (int)this.data[(startOffset - 1) + average * this.getItemLength() + 2];
            if(score == compare) {
                return average;
            } else if(score > compare){
                min = average + 1;
            }else if(score < compare){
                max = average - 1;
            }
        }
        return -1;
    }

    private int findInsertOffset(int score){
        int min = 0;
        int max = (this.getTailOffset() - startOffset + 1) / this.getItemLength();
        while(min <= max){
            int average = (max + min) / 2;
            int compare = (int)this.data[average * this.getItemLength() + startOffset - 1];

            if(score == compare) {
                break;
            } else if(score > compare){
                min = average + 1;
            }else if(score < compare){
                max = average - 1;
            }
        }
        return startOffset + (min - 1) * this.getItemLength();
    }

    private void resize(){
        int size = this.getTotalSize() << 1;
        Object[] tempArr = new Object[size];
        System.arraycopy(this.data, 0, tempArr, 0, this.data.length);
        tempArr[0] = size;
        this.data = tempArr;
    }

    private int getTotalSize(){
        return (int) this.data[0];
    }

    private int getItemLength(){
        return (int) this.data[2];
    }

    private int getTailOffset(){
        return (int) this.data[1];
    }

    private void setTailOffset(int i){
        this.data[1] = i;
    }

    private int size(){
        return (this.getTailOffset() - (this.startOffset - 1)) / 2;
    }
    public static void main(String[] args) {
        SortedZipList<String> sortedZipList = new SortedZipList<>();
        sortedZipList.add(1,"a");
        sortedZipList.add(2,"b");
        sortedZipList.add(6,"f");
        sortedZipList.add(7,"g");
        sortedZipList.add(7,"g");
        sortedZipList.add(3,"c");
        sortedZipList.add(4,"d");
        sortedZipList.add(5,"e");
        sortedZipList.add(8,"h");
        sortedZipList.add(9,"i");

        System.out.println(sortedZipList.get(7));
        System.out.println("remove: " + sortedZipList.remove(7));
        System.out.println("remove: " + sortedZipList.remove(1));
        System.out.println(sortedZipList.get(7));
        System.out.println(sortedZipList.size());
        System.out.println("get first: " + sortedZipList.getFirst());
        System.out.println("remove first: " + sortedZipList.removeFirst());
        System.out.println("remove first: " + sortedZipList.removeFirst());
    }
}
