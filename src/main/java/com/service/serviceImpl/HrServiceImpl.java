package com.service.serviceImpl;

import com.comparators.HrComparator;
import com.model.Hr;
import com.model.UserDetail;
import com.repository.HrRepository;
import com.service.EmployeeService;
import com.service.HrService;
import com.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HrServiceImpl implements HrService {
    private final HrRepository hrRepository;
    private final VacancyService vacancyService;
    private final EmployeeService employeeService;
    @Autowired
    public HrServiceImpl(HrRepository hrRepository,VacancyService vacancyService,EmployeeService employeeService){
        this.hrRepository = hrRepository;
        this.vacancyService=vacancyService;
        this.employeeService=employeeService;
    }
    @Override
    public Hr addAndUpdateHr(Hr hr){
        return hrRepository.save(hr);
    }
    @Override
    public List<Hr> getHrs(){
        List<Hr> hrs = hrRepository.findAll();
        hrs.sort(new HrComparator());
        return hrs;
    }
    @Override
    public Hr findHrById(int id){

        return hrRepository.findById(id).orElse(null);
    }

    @Override
    public Hr findHrByUserDetail(UserDetail userDetail){

        return hrRepository.findByUserDetail(userDetail);
    }
    @Override
    public void deleteHr(int id){
        hrRepository.deleteById(id);
    }

    @Override
    public void deleteHrByUserDetail(UserDetail userDetail){
        List<Hr> hrs =hrRepository.findAll();
        if(!(hrs.isEmpty())){
            for(Hr hr:hrs){
                if(hr.getUserDetail().getIdUserDetails()==userDetail.getIdUserDetails()){
                    deleteHr(hr.getIdHr());
                    return;
                }
            }
        }
    }
}
