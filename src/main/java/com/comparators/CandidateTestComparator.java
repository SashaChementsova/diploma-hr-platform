package com.comparators;

import com.model.Candidate;

import java.util.Comparator;
import java.util.Objects;

public class CandidateTestComparator implements Comparator<Candidate> {
    @Override
    public int compare(Candidate c1, Candidate c2){
        float result1=(c1.getTrialEntities().get(0).getResultTesting().getPositionTest().getResult().getPoints()+c1.getTrialEntities().get(0).getResultTesting().getLanguageTestEntities().get(0).getResult().getPoints())/2;
        float result2=(c2.getTrialEntities().get(0).getResultTesting().getPositionTest().getResult().getPoints()+c2.getTrialEntities().get(0).getResultTesting().getLanguageTestEntities().get(0).getResult().getPoints())/2;
        if (result1== result2)
            return 0;
        else if (result1 < result2)
            return 1;
        else
            return -1;
    }
}
