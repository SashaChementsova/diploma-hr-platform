package com.service.serviceImpl;

import com.comparators.PositionNameComparator;
import com.model.*;
import com.repository.*;
import com.service.PositionNameService;
import com.service.PositionTestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class PositionNameServiceImpl implements PositionNameService {
    private final PositionNameRepository positionNameRepository;


    private final DepartmentRepository departmentRepository;

    private final VacancyRepository vacancyRepository;

    private final PositionTestQuestionService positionTestQuestionService;

    @Autowired
    public PositionNameServiceImpl(PositionNameRepository positionNameRepository, DepartmentRepository departmentRepository,  VacancyRepository vacancyRepository,PositionTestQuestionService positionTestQuestionService){
        this.positionNameRepository = positionNameRepository;
        this.departmentRepository=departmentRepository;
        this.vacancyRepository=vacancyRepository;
        this.positionTestQuestionService=positionTestQuestionService;
    }
    @Override
    public PositionName addAndUpdatePositionName(PositionName positionName){
        return positionNameRepository.save(positionName);
    }
    @Override
    public List<PositionName> getPositionNames(){
        List<PositionName> positionNames = positionNameRepository.findAll();
        positionNames.sort(new PositionNameComparator());
        return positionNames;
    }
    @Override
    public PositionName findPositionNameById(int id){

        return positionNameRepository.findById(id).orElse(null);
    }
    @Override
    public void deletePositionName(int id){
        positionNameRepository.deleteById(id);
    }

    @Override
    public void initializePositionName(){
        try {
            Department department= departmentRepository.save(new Department("Отдел кадров"));
            positionNameRepository.save(new PositionName("HR-менеджер",department));
        }
        catch (Exception ex){
            System.out.println("Значения уже есть");
        }
    }


    @Override
    public List<Vacancy> getVacanciesByPositionName(int id){
        PositionName positionName=positionNameRepository.findById(id).orElse(null);
        if(positionName==null) return null;
        List<Vacancy> allVacancies=vacancyRepository.findAll();
        List<Vacancy> vacancies=new ArrayList<>();
        for(Vacancy vacancy:allVacancies){
            if(vacancy.getPosition().getPositionName().getName().equals(positionName.getName())){
                vacancies.add(vacancy);
            }
        }
        return vacancies;
    }

    @Override
    public boolean checkPositionNameByVacancy(int id){
        List<Vacancy> vacancies=getVacanciesByPositionName(id);
        if(vacancies!=null){
            if(!(vacancies.isEmpty())){
                for(Vacancy vacancy:vacancies){
                    if(vacancy.getStatus().equals("В процессе")) return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkPositionNameByEmployee(int id){
        PositionName positionName=findPositionNameById(id);
        if(positionName!=null){
            List<Position> positions=positionName.getPositionEntities();
            if(positions!=null){
                if(!(positions.isEmpty())){
                    List<Employee> employees=new ArrayList<>();
                    for(Position position:positions){
                        employees=position.getEmployeeEntities();
                        if(employees!=null){
                            if(!(employees.isEmpty())){
                                for(Employee employee:employees){
                                    if(employee.getPosition().getPositionName().getIdPositionName()==id)
                                        return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    @Override
    public boolean checkPositionTestByPositionName(PositionName positionName) {
        List<PositionTestQuestion> positionTestQuestionList = positionTestQuestionService.getPositionTestQuestionsByPositionName(positionName);
        if (positionTestQuestionList!=null) {
            if(!(positionTestQuestionList.isEmpty()))
                return positionTestQuestionService.checkDateOfPositionTestByQuestions(positionTestQuestionList);
        }
        return true;
    }


}
