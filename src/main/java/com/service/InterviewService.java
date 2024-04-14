package com.service;

import com.model.Interview;

import java.util.List;

public interface InterviewService {
    public Interview addAndUpdateInterview(Interview interview);
    public List<Interview> getInterviews();

    public Interview findInterviewById(int id);
    public void deleteInterview(int id);
    public List<Interview> getActiveInterviews(List<Interview> interviews);
}
