package com.comparators;

import com.model.Vacancy;

import java.util.Comparator;
import java.util.Objects;

public class VacancyComparator implements Comparator<Vacancy> {
    @Override
    public int compare(Vacancy v1, Vacancy v2){
        if(Objects.equals(v1.getPosition().toString(), v2.getPosition().toString())){
            return 0;
        }
        if(v1.getPosition().toString()==null) return -1;
        if(v2.getPosition().toString()==null) return 1;
        return v1.getPosition().toString().compareTo(v2.getPosition().toString());
    }
}