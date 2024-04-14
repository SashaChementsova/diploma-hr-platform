package com.service;

import com.model.Trial;
import com.model.Vacancy;

import java.util.List;

public interface TrialService {
    public Trial addAndUpdateTrial(Trial trial);
    public List<Trial> getTrials();

    public Trial findTrialById(int id);
    public void deleteTrial(int id);
    public List<Trial> getActiveTrial(Vacancy vacancy);
}
