package com.service;

import com.model.EducationType;

import java.util.List;

public interface EducationTypeService {
    public EducationType addAndUpdateEducationType(EducationType educationType);
    public List<EducationType> getEducationTypes();

    public EducationType findEducationTypeById(int id);
    public void deleteEducationType(int id);
}
