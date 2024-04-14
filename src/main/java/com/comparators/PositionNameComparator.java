package com.comparators;

import com.model.PositionName;

import java.util.Comparator;
import java.util.Objects;

public class PositionNameComparator implements Comparator<PositionName> {
    @Override
    public int compare(PositionName p1, PositionName p2){
        if(Objects.equals(p1.getName(), p2.getName())){
            return 0;
        }
        if(p1.getName()==null) return -1;
        if(p2.getName()==null) return 1;
        return p1.getName().compareTo(p2.getName());
    }
}