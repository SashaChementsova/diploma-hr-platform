package com.service.serviceImpl;

import com.comparators.DepartmentComparator;
import com.model.*;
import com.repository.DepartmentRepository;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final PositionNameService positionNameService;

    private final PositionService positionService;

    private final PositionTestQuestionService positionTestQuestionService;

    private final EmployeeService employeeService;
    private final HrService hrService;
    @Autowired
    public DepartmentServiceImpl(HrService hrService,DepartmentRepository departmentRepository,PositionNameService positionNameService,PositionService positionService,PositionTestQuestionService positionTestQuestionService,EmployeeService employeeService){
        this.departmentRepository = departmentRepository;
        this.positionNameService=positionNameService;
        this.positionService=positionService;
        this.positionTestQuestionService=positionTestQuestionService;
        this.employeeService=employeeService;
        this.hrService=hrService;
    }
    @Override
    public Department addAndUpdateDepartment(Department department){
        return departmentRepository.save(department);
    }
    @Override
    public List<Department> getDepartments(){
        List<Department> departments = departmentRepository.findAll();
        departments.sort(new DepartmentComparator());
        return departments;
    }
    @Override
    public Department findDepartmentById(int id){

        return departmentRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteDepartment(int id){
        departmentRepository.deleteById(id);
    }



    @Override
    public void initializeDepartment(){
        try {

            Department department= departmentRepository.save(new Department("Отдел кадров"));
            positionNameService.addAndUpdatePositionName(new PositionName("HR-менеджер",department));
        }
        catch (Exception ex){
            System.out.println("Значения уже есть");
        }
    }

    @Override
    public List<Employee> getEmployeesByDepartment(int idDepartment){
        Department department=departmentRepository.findById(idDepartment).orElse(null);
        if(department==null) return null;
        List<PositionName> positionNames=department.getPositionNameEntities();
        List<Employee>employeesByDepartment=new ArrayList<>();
        if(positionNames!=null) {
            if (!(positionNames.isEmpty())) {
                for(PositionName positionName:positionNames){
                    employeesByDepartment.addAll(employeeService.getEmployeesByPositionName(positionName));
                }
            }
        }
        return employeesByDepartment;
    }

    @Override
    public boolean checkDepartmentByEmployees(Department department){
        List<Employee> employees=getEmployeesByDepartment(department.getIdDepartment());
        if(employees!=null) {
            if (!(employees.isEmpty())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkDepartmentByVacancies(Department department){
        List<PositionName> positionNames=department.getPositionNameEntities();
        List<Vacancy> vacancies;
        if(positionNames!=null) {
            if (!(positionNames.isEmpty())) {
                for(PositionName positionName:positionNames){
                    vacancies=positionNameService.getVacanciesByPositionName(positionName.getIdPositionName());
                    if(vacancies==null || vacancies.isEmpty()) continue;
                    for(Vacancy vacancy:vacancies){
                        if(vacancy.getStatus().equals("В процессе")) return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkDepartmentByTestQuestions(Department department){
        List<PositionName> positionNames=department.getPositionNameEntities();
        if(positionNames!=null){
            if(!(positionNames.isEmpty())){
                for(PositionName positionName:positionNames){
                    if(!(positionNameService.checkPositionTestByPositionName(positionName))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void deletePositionNameByDepartment(Department department){
        List<PositionName> positionNames =department.getPositionNameEntities();
        if(positionNames!=null){
            if(!(positionNames.isEmpty())){
                for(PositionName positionName:positionNames){
                    positionNameService.deletePositionName(positionName.getIdPositionName());
                }
            }
        }
    }

    @Override
    public void deletePositionByDepartment(Department department){
        List<PositionName> positionNames =department.getPositionNameEntities();
        if(positionNames!=null){
            if(!(positionNames.isEmpty())){
                for(PositionName positionName:positionNames){
                    positionService.deletePositionByPositionName(positionName);
                }
            }
        }
    }

    @Override
    public void deleteTestQuestionByDepartment(Department department){
        List<PositionName> positionNames =department.getPositionNameEntities();
        if(positionNames!=null) {
            if(!(positionNames.isEmpty())){
                for(PositionName positionName:positionNames){
                    positionTestQuestionService.deleteQuestionsByPositionName(positionName);
                }
            }
        }

    }

    @Override
    public void deleteEmployeesByDepartment(Department department){
        List<PositionName> positionNames =department.getPositionNameEntities();
        if(positionNames!=null) {
            if(!(positionNames.isEmpty())){
                for(PositionName positionName:positionNames){
                    employeeService.deleteEmployeesByPositionName(positionName);
                }
            }
        }

    }

    @Override
    public void deleteHrByDepartment(Department department){
        List<PositionName> positionNames =department.getPositionNameEntities();
        if(positionNames!=null) {
            if(!(positionNames.isEmpty())){
                for(PositionName positionName:positionNames){
                    List<Employee> employees=employeeService.getEmployeesByPositionName(positionName);
                    if(employees!=null){
                        if(!(employees.isEmpty())){
                            for(Employee employee:employees){
                                hrService.deleteHrByUserDetail(employee.getUserDetail());
                            }
                        }
                    }
                }
            }
        }

    }
}
