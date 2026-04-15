package minMaxHeap.lib.base;


public class MinMaxHeap<E extends Comparable<E>> implements InterMInMaxHeap<E> {
    private E[] arr;
    private int count;
    private final int DEFAULT_CAPACITY = 63;


    public MinMaxHeap() {
        this.arr = (E[]) new Comparable[DEFAULT_CAPACITY];
    }
    public MinMaxHeap(int maxSize) {
        arr = (E[]) new Comparable[maxSize];
    }

    public MinMaxHeap(E[] arrayToHeapify) {
        this.arr = arrayToHeapify; //maybe copy, here operates on the same array, yet works
        count=arr.length;
        for (int i=(count-1)/2; i>=0; i--) {
            if((Math.floor(Math.log(i + 1)/Math.log(2)))%2==0) pushDownMin(i);
            else pushDownMax(i);
        }
    }

    @Override
    public E getMax() {
        if(count==0) return null; //cannot predecrement here, because after count=-1
        if(--count==0) return arr[0];
        if(count==1) return arr[1]; //after this line, there are at least 3 elems
        var maxIndex=arr[2].compareTo(arr[1]) > 0 ? 2 : 1;
        var max=arr[maxIndex];
        arr[maxIndex]=arr[count];
        pushDownMax(maxIndex);
        return max;
    }

    @Override
    public E getMin() {
        if(count==0) return null;
        var min= arr[0];
        arr[0]=arr[--count];
        pushDownMin(0);
        return min;
    }
    @Override
    public E getByIndex(int index) {
        if(index<0 || index>=count) return null;
        var tmp=arr[index];
        arr[index]=arr[--count];
        if(index>2) pushUp(index, (index-1)/2);
        if((Math.floor(Math.log(index + 1)/Math.log(2)))%2==0) pushDownMin(index);
        else pushDownMax(index);
        return tmp;
    }
    @Override
    public E getByRandom() {
        int v =(int)(Math.random() * count);//random in [1,0)
       // System.out.println(v);
         return getByIndex(v);
    }
    

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean isFull() {
        return arr.length == count;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public E peekMin() {
        return (count==0) ? null : arr[0];
    }

    @Override
    public E peekMax() {
        if (count==0) return null;
        if (count==1) return arr[0];
        if (count==2) return arr[1];
        return (arr[1].compareTo(arr[2]) > 0) ? arr[1] : arr[2];
    }

    @Override
    public E peekByIndex(int index) {
        return (count>index) ? null : arr[index];
    }

    @Override
    public boolean insert(E e) {
        if (e == null) throw new IllegalArgumentException();
        if (isFull()) return false;
        arr[count++] = e; //dodaj na koniec
        if (count > 1) {
            int child = count - 1;
            int parent = (child - 1) / 2;
            pushUp(child, parent);
        }
        return true;
    }

    private void pushUp(int child, int parent) {
        if ( (Math.floor(Math.log(child + 1)/Math.log(2))) % 2 == 0) {//on minlevel
            if (arr[child].compareTo(arr[parent]) > 0) {
                var tmp = arr[child];     //swap with parent
                arr[child] = arr[parent];
                arr[parent] = tmp;
                pushUpMax(parent);
            } else {
                pushUpMin(child);
            }
        } else {//on maxlevel
            if (arr[child].compareTo(arr[parent]) < 0) {
                var tmp = arr[child];
                arr[child] = arr[parent];
                arr[parent] = tmp;
                pushUpMin(parent);
            } else {
                pushUpMax(child);
            }
        }
    }

    private void pushUpMin(int node) {
        int grandpa = ((node - 1) / 2 - 1) / 2;
        if (node >= 3 && arr[node].compareTo(arr[grandpa]) < 0) {
            var tmp = arr[node];
            arr[node] = arr[grandpa];
            arr[grandpa] = tmp;
            pushUpMin(grandpa);
        }
    }

    private void pushUpMax(int node) {
        int grandpa = ((node - 1) / 2 - 1) / 2;
        if (node >= 3 && arr[node].compareTo(arr[grandpa]) > 0) {
            var tmp = arr[node];
            arr[node] = arr[grandpa];
            arr[grandpa] = tmp;
            pushUpMax(grandpa);
        }
    }

    private int getSmallestDescendantsIndex(int node) {
        int[] descendants = {node*2 + 1, node*2 + 2, (node*2 + 1)*2 + 1, (node*2 + 1)*2 + 2, (node*2 + 2)*2 + 1, (node*2 + 2)*2 + 2};
        int smallest = descendants[0];
        for (int i = 1; i < descendants.length; i++) {
            if (descendants[i] > count - 1) break;
            if (arr[descendants[i]].compareTo(arr[smallest]) < 0) smallest = descendants[i];
        }
        return smallest;
    }
    private int getBiggestDescendantsIndex(int node) {
        int[] descendants = {node*2 + 1, node*2 + 2, (node*2 + 1)*2 + 1, (node*2 + 1)*2 + 2, (node*2 + 2)*2 + 1, (node*2 + 2)*2 + 2};
        int biggest = descendants[0];
        for (int i = 1; i < descendants.length; i++) {
            if (descendants[i] > count - 1) break;
            if (arr[descendants[i]].compareTo(arr[biggest]) > 0) biggest = descendants[i];
        }
        return biggest;
    }


    private void pushDownMin(int node) {
        if (node*2 + 1 < count - 1) { //czy  ma dzieci?
            int small = getSmallestDescendantsIndex(node);
            if (arr[small].compareTo(arr[node]) < 0) {
                var tmp = arr[node]; //najmniejszy element na swoje miejsce
                arr[node] = arr[small];
                arr[small] = tmp;
                if (small > node*2 + 2) { //indeks większy niż drugie dziecko == któryś wnuk
                    if (arr[small].compareTo(arr[(small - 1) / 2]) > 0) { //po zamianie - czy element nie jest większy niż rodzic (największy z poddrzewa)
                        tmp = arr[small]; //największy element poddrzewa na swoje miejsce
                        arr[small] = arr[(small - 1) / 2];
                        arr[(small - 1) / 2] = tmp;
                    }
                    pushDownMin(small);
                }
            }
        }
    }

    private void pushDownMax(int node) {
        if (node*2 + 1 < count - 1) { //czy  ma dzieci?
            int big=getBiggestDescendantsIndex(node);
        if (arr[big].compareTo(arr[node])>0){
            var tmp=arr[big];
            arr[big]=arr[node];
            arr[node]=tmp;
            if(big>node*2 + 2){
                if(arr[big].compareTo(arr[(big-1)/2])<0){
                    tmp=arr[big];
                    arr[big]=arr[(big-1)/2];
                    arr[(big-1)/2] = tmp;
                }
                pushDownMax(big);
            }
        }




        }
    }



}
