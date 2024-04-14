package com.service;

import com.model.Employee;
import com.model.PositionName;
import com.model.Vacancy;

import java.util.List;

public interface PositionNameService {
    public PositionName addAndUpdatePositionName(PositionName positionName);
    public List<PositionName> getPositionNames();

    public PositionName findPositionNameById(int id);
    public void deletePositionName(int id);

    public void initializePositionName();

    public boolean checkPositionNameByEmployee(int id);

    public boolean checkPositionNameByVacancy(int id);
    public List<Vacancy> getVacanciesByPositionName(int id);
    public  boolean checkPositionTestByPositionName(PositionName positionName);
}
