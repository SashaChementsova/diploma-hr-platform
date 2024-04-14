package com.comparators;

import com.model.Employee;

import java.util.Comparator;
import java.util.Objects;

public class EmployeeComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2){
        if(Objects.equals(e1.getUserDetail().getSNP(), e2.getUserDetail().getSNP())){
            return 0;
        }
        if(e1.getUserSNP()==null) return -1;
        if(e2.getUserSNP()==null) return 1;
        return e1.getUserDetail().getSNP().compareTo(e2.getUserDetail().getSNP());
    }
}
