package com.comparators;

import com.model.Hr;

import java.util.Comparator;
import java.util.Objects;

public class HrComparator implements Comparator<Hr> {
    @Override
    public int compare(Hr h1, Hr h2){
        if(Objects.equals(h1.getUserDetail().getSNP(), h2.getUserDetail().getSNP())){
            return 0;
        }
        if(h1.getUserDetail().getSNP()==null) return -1;
        if(h2.getUserDetail().getSNP()==null) return 1;
        return h1.getUserDetail().getSNP().compareTo(h2.getUserDetail().getSNP());
    }
}