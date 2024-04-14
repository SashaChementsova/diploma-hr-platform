package com.service.serviceImpl;

import com.comparators.EmployeeComparator;
import com.model.*;
import com.repository.BackgroundRepository;
import com.repository.EducationRepository;
import com.repository.EmployeeRepository;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionNameService positionNameService;

    private final BackgroundRepository backgroundRepository;
    private final EducationRepository educationRepository;

    private final UserDetailsHasChatService userDetailsHasChatService;

    private final UserDetailService userDetailService;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,PositionNameService positionNameService,BackgroundRepository backgroundRepository,EducationRepository educationRepository,UserDetailsHasChatService userDetailsHasChatService,UserDetailService userDetailService){
        this.employeeRepository = employeeRepository;
        this.positionNameService=positionNameService;
        this.backgroundRepository=backgroundRepository;
        this.educationRepository=educationRepository;
        this.userDetailsHasChatService=userDetailsHasChatService;
        this.userDetailService=userDetailService;
    }
    @Override
    public Employee addAndUpdateEmployee(Employee employee){
        return employeeRepository.save(employee);
    }
    @Override
    public List<Employee> getEmployees(){
        List<Employee> employees=employeeRepository.findAll();
        employees.sort(new EmployeeComparator());
        return employees;
    }
    @Override
    public Employee findEmployeeById(int id){

        return employeeRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteEmployee(int id){
        employeeRepository.deleteById(id);
    }

    @Override
    public int calculateDifferenceDates(Employee employee){
        int age;
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateRecruitment());
        String date2 = new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateContractEnd());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = format.parse(date1);
            dateTwo = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Количество дней между датами в миллисекундах
        long difference = dateTwo.getTime() - dateOne.getTime();
        // Перевод количества дней между датами из миллисекунд в дни
        double days = difference / (24 * 60 * 60 * 1000); // миллисекунды / (24ч * 60мин * 60сек * 1000мс)
        double years=days/365;
        age= (int) Math.floor(years);
        return age;
    }

    @Override
    public int compareDates(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = format.parse(date1);
            dateTwo = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateOne.compareTo(dateTwo);
    }
    @Override
    public List<Employee> findEmployeeBySNP(String SNP, PositionName positionName){
        List<Employee> employees=getEmployees();
        List<Employee> employees1=new ArrayList<>();
        for (Employee employee:employees) {
            if(employee.getUserDetail().getSNP().contains(SNP)&&positionName.getIdPositionName()==employee.getPosition().getPositionName().getIdPositionName()){
                employees1.add(employee);
            }
        }
        return employees1;
    }
    @Override
    public List<Employee> findEmployeeBySNP(String SNP){
        List<Employee> employees=getEmployees();
        List<Employee> employees1=new ArrayList<>();
        for (Employee employee:employees) {
            if(employee.getUserDetail().getSNP().contains(SNP)){
                employees1.add(employee);
            }
        }
        return employees1;
    }




    @Override
    public void deleteEmployeesByPositionName(PositionName positionName){
        List<Employee> employees=getEmployeesByPositionName(positionName);
        if(employees!=null&&!(employees.isEmpty())){
            for(Employee employee:employees){
                Background background=employee.getBackground();
                List<Education> educations=employee.getEducationEntities();
                if(educations!=null){
                    if(!(educations.isEmpty())){
                        for(Education education:educations){
                            educationRepository.deleteById(education.getIdEducation());
                        }
                    }
                }
                int id=employee.getUserDetail().getIdUserDetails();
                userDetailsHasChatService.deleteChatByUserDetail(employee.getUserDetail());
                deleteEmployee(employee.getIdEmployee());
                backgroundRepository.deleteById(background.getIdBackground());
                userDetailService.deleteUser(id);
            }
        }
    }
    @Override
    public List<Employee> getEmployeesByPositionName(PositionName positionName){
        if(positionName==null) return null;
        List<Employee> allEmployees=getEmployees();
        List<Employee> employees=new ArrayList<>();
        for(Employee employee:allEmployees){
            if(employee.getPosition().getPositionName().getName().equals(positionName.getName())){
                employees.add(employee);
            }
        }
        return employees;

    }

    @Override
    public Employee getEmployeeByHr(Hr hr){
        return hr.getUserDetail().getEmployee();
    }

    @Override
    public List<Employee> getEmployeesUnderPosition(Position position){
        List<Employee> resultEmployees=new ArrayList<>();
        List<Employee> employees=getEmployeesByPositionName(position.getPositionName());
        if(employees!=null){
            if(!(employees.isEmpty())){
                for(Employee employee:employees){
                    if(employee.getPosition().getLevelPosition().getIdLevelPosition()>=position.getLevelPosition().getIdLevelPosition()){
                        resultEmployees.add(employee);
                    }
                }
            }
        }
        return resultEmployees;
    }
}
