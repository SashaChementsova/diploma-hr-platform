package com.service;

import com.model.Hr;
import com.model.Position;
import com.model.PositionName;
import com.model.Vacancy;

import java.util.List;

public interface VacancyService {
    public Vacancy addAndUpdateVacancy(Vacancy vacancy);
    public List<Vacancy> getVacancies();

    public List<Vacancy> getActiveVacancies();
    public List<Vacancy> getActiveVacanciesByPosition(Position position);

    public Vacancy findVacancyById(int id);
    public void deleteVacancy(int id);

    public List<Vacancy> getVacanciesByPositionName(PositionName positionName);
    public void deleteVacancyByPositionName(PositionName positionName);

    public void deleteVacancyByHr(Hr hr);
    public List<Vacancy> findVacanciesByPosition(Position position);
    public List<Vacancy> findHrVacanciesByPosition(List<Vacancy> vacancies,Position position);
}
