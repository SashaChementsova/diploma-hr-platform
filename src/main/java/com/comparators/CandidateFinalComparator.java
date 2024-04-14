package com.comparators;

import com.dto.CandidatesFinals;

import java.util.Comparator;

public class CandidateFinalComparator implements Comparator<CandidatesFinals> {
    @Override
    public int compare(CandidatesFinals c1, CandidatesFinals c2) {
        float result1 = c1.getPoints();
        float result2=c2.getPoints();
        if (result1 == result2)
            return 0;
        else if (result1 < result2)
            return 1;
        else
            return -1;
    }
}
