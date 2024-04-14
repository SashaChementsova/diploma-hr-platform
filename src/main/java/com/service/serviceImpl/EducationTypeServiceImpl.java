package com.service.serviceImpl;

import com.model.EducationType;
import com.repository.EducationTypeRepository;
import com.service.EducationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EducationTypeServiceImpl implements EducationTypeService {
    private final EducationTypeRepository educationTypeRepository;
    @Autowired
    public EducationTypeServiceImpl(EducationTypeRepository educationTypeRepository){
        this.educationTypeRepository = educationTypeRepository;
    }
    @Override
    public EducationType addAndUpdateEducationType(EducationType educationType){
        return educationTypeRepository.save(educationType);
    }
    @Override
    public List<EducationType> getEducationTypes(){
        return educationTypeRepository.findAll();
    }
    @Override
    public EducationType findEducationTypeById(int id){

        return educationTypeRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteEducationType(int id){
        educationTypeRepository.deleteById(id);
    }
}
