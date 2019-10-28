package com.example.emptytest.Generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SortableList<E> extends ArrayList<E> {

    private boolean autoSorting = false;
    private Comparator <? super E> autoComparator = null;

    public SortableList(){
        super();
    }

    public SortableList(Comparator <? super E> autoComparator){
        super();
        this.autoComparator = autoComparator;
    }

    public void sort() throws NullPointerException{
        if (autoComparator != null){
            sort(autoComparator);
        }else {
            throw new NullPointerException("AutoComparator has not been set.");
        }
    }

    @Override
    public void sort(Comparator<? super E> comparator){
        int sortedFromIdx = this.size();
        while(sortedFromIdx != 0){
            int swappedIdx = 0;
            for (int i = 1; i < sortedFromIdx; i++) {
                if (comparator.compare(get(i), get(i-1)) == 1){
                    E firstElement = get(i-1);
                    remove(firstElement);
                    add(i, firstElement);
                    swappedIdx = i;
                }
            }
            sortedFromIdx = swappedIdx;
        }
    }

    public boolean isAutoSorting(){
        return autoSorting;
    }

    public void setAutoSorting(boolean input){
        autoSorting = input;
    }

    public Comparator<? super E> getAutoComparator(){
        return autoComparator;
    }

    public void setAutoComparator (Comparator<? super E> input){
        autoComparator = input;
    }

    @Override
    public boolean add(E item){
        boolean res = super.add(item);
        if (autoComparator != null){
            sort(autoComparator);
        }
        return res;
    }

    @Override
    public boolean addAll(Collection<? extends E> items){
        boolean res = super.addAll(items);
        if (autoComparator != null){
            sort(autoComparator);
        }
        return res;
    }


}
