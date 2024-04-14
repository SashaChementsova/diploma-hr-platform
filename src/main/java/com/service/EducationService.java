package com.service;

import com.model.Education;

import java.util.List;

public interface EducationService{

    public Education addAndUpdateEducation(Education education);
    public List<Education> getEducations();

    public Education findEducationById(int id);
    public void deleteEducation(int id);

    public void deleteEducations(List<Education> educations);
}
