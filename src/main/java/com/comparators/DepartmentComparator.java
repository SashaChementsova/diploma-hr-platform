package com.comparators;

import com.model.Department;

import java.util.Comparator;
import java.util.Objects;

public class DepartmentComparator implements Comparator<Department> {
@Override
public int compare(Department d1, Department d2){
        if(Objects.equals(d1.getNameDepartment(), d2.getNameDepartment())){
        return 0;
        }
        if(d1.getNameDepartment()==null) return -1;
        if(d2.getNameDepartment()==null) return 1;
        return d1.getNameDepartment().compareTo(d2.getNameDepartment());
        }
}