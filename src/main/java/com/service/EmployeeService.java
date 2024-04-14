package com.service;

import com.model.Employee;
import com.model.Hr;
import com.model.Position;
import com.model.PositionName;

import java.util.List;

public interface EmployeeService {
    public Employee addAndUpdateEmployee(Employee employee);
    public List<Employee> getEmployees();

    public Employee findEmployeeById(int id);
    public void deleteEmployee(int id);

    public int calculateDifferenceDates(Employee employee);
    public Employee getEmployeeByHr(Hr hr);
    public int compareDates(String date1,String date2);
    public List<Employee> findEmployeeBySNP(String SNP,PositionName positionName);
    public List<Employee> findEmployeeBySNP(String SNP);
    public void deleteEmployeesByPositionName(PositionName positionName);

    public List<Employee> getEmployeesByPositionName(PositionName positionName);

    public List<Employee> getEmployeesUnderPosition(Position position);
}
