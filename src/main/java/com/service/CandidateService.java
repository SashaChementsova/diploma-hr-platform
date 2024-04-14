package com.service;

import com.model.Candidate;
import com.model.Trial;

import java.util.List;

public interface CandidateService {

    public Candidate addAndUpdateCandidate(Candidate candidate);

    public List<Candidate> getCandidates();
    public List<Candidate> getCandidatesByTesting();

    public List<Candidate> sortCandidatesByTesting(List<Candidate> candidates);

    public Candidate findCandidateById(int id);

    public void deleteCandidate(int id);

    public boolean checkActiveTrialOfCandidate(Candidate candidate);

    public void checkCandidatesByTestsAndInterview();
    public void deleteCandidateTrial(Trial trial);
}
