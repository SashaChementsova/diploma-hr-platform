package com.comparators;
import com.model.CityCompany;

import java.util.Comparator;
import java.util.Objects;

public class CityCompanyComparator implements Comparator<CityCompany>{
        @Override
        public int compare(CityCompany c1, CityCompany c2){
            if(Objects.equals(c1.getNameCity(), c2.getNameCity())){
                return 0;
            }
            if(c1.getNameCity()==null) return -1;
            if(c2.getNameCity()==null) return 1;
            return c1.getNameCity().compareTo(c2.getNameCity());
        }
}
