package com.comparators;

import com.model.Candidate;
import java.util.Comparator;
import java.util.Objects;

public class CandidateComparator implements Comparator<Candidate> {
    @Override
    public int compare(Candidate c1, Candidate c2){
        if(Objects.equals(c1.getUserDetail().getSNP(), c2.getUserDetail().getSNP())){
            return 0;
        }
        if(c1.getUserDetail().getSNP()==null) return -1;
        if(c2.getUserDetail().getSNP()==null) return 1;
        return c1.getUserDetail().getSNP().compareTo(c2.getUserDetail().getSNP());
    }
}
