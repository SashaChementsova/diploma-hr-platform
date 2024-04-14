package com.service;


import com.model.Department;
import com.model.Employee;

import java.util.List;

public interface DepartmentService {

    public Department addAndUpdateDepartment(Department department);
    public List<Department> getDepartments();

    public Department findDepartmentById(int id);
    public void deleteDepartment(int id);

    public void initializeDepartment();
    public List<Employee> getEmployeesByDepartment(int idDepartment);
    public boolean checkDepartmentByEmployees(Department department);

    public boolean checkDepartmentByVacancies(Department department);

    public boolean checkDepartmentByTestQuestions(Department department);

    public void deletePositionNameByDepartment(Department department);

    public void deletePositionByDepartment(Department department);

    public void deleteTestQuestionByDepartment(Department department);

    public void deleteEmployeesByDepartment(Department department);

    public void deleteHrByDepartment(Department department);
}
