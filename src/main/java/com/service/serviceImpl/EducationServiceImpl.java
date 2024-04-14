package com.service.serviceImpl;

import com.model.Education;
import com.repository.EducationRepository;
import com.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;
    @Autowired
    public EducationServiceImpl(EducationRepository educationRepository){
        this.educationRepository = educationRepository;
    }
    @Override
    public Education addAndUpdateEducation(Education education){
        return educationRepository.save(education);
    }
    @Override
    public List<Education> getEducations(){
        return educationRepository.findAll();
    }
    @Override
    public Education findEducationById(int id){

        return educationRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteEducation(int id){
        educationRepository.deleteById(id);
    }
    @Override
    public void deleteEducations(List<Education> educations){
        if(educations!=null){
            if(!(educations.isEmpty())){
                for(Education education:educations){
                    deleteEducation(education.getIdEducation());
                }
            }
        }
    }
}
