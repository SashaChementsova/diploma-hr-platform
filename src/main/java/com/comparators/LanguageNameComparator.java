package com.comparators;

import com.model.LanguageName;

import java.util.Comparator;
import java.util.Objects;

public class LanguageNameComparator implements Comparator<LanguageName> {

    @Override
    public int compare(LanguageName o1, LanguageName o2) {
        if(Objects.equals(o1.getName(),o2.getName())) return 0;
        if(o1.getName()==null) return -1;
        if(o2.getName()==null) return 1;
        return o1.getName().compareTo(o2.getName());
    }
}
