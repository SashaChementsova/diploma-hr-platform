package com.service.serviceImpl;

import com.comparators.CandidateComparator;
import com.comparators.CandidateTestComparator;
import com.model.*;
import com.repository.CandidateRepository;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final PositionTestHasQuestionService positionTestHasQuestionService;
    private final LanguageTestHasQuestionService languageTestHasQuestionService;
    private final TrialService trialService;
    private final InterviewService interviewService;

    private final ResultService resultService;
    private final ResultTestingService resultTestingService;
    private final PositionTestService positionTestService;
    private final LanguageTestService languageTestService;
    @Autowired
    public CandidateServiceImpl(CandidateRepository candidateRepository, PositionTestHasQuestionService positionTestHasQuestionService, LanguageTestHasQuestionService languageTestHasQuestionService, TrialService trialService, InterviewService interviewService, ResultService resultService, ResultTestingService resultTestingService, PositionTestService positionTestService, LanguageTestService languageTestService) {
        this.candidateRepository = candidateRepository;
        this.positionTestHasQuestionService = positionTestHasQuestionService;
        this.languageTestHasQuestionService = languageTestHasQuestionService;
        this.trialService = trialService;
        this.interviewService = interviewService;
        this.resultService = resultService;
        this.resultTestingService = resultTestingService;
        this.positionTestService = positionTestService;
        this.languageTestService = languageTestService;
    }

    @Override
    public Candidate addAndUpdateCandidate(Candidate candidate){
        return candidateRepository.save(candidate);
    }
    @Override
    public List<Candidate> getCandidates(){
        List<Candidate> candidates = candidateRepository.findAll();
        candidates.sort(new CandidateComparator());
        return candidates;
    }
    @Override
    public List<Candidate> getCandidatesByTesting(){
        List<Candidate> candidates = candidateRepository.findAll();
        candidates.sort(new CandidateTestComparator());
        return candidates;
    }

    @Override
    public List<Candidate> sortCandidatesByTesting(List<Candidate> candidates){
        candidates.sort(new CandidateTestComparator());
        return candidates;
    }
    @Override
    public Candidate findCandidateById(int id){

        return candidateRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteCandidate(int id){
        candidateRepository.deleteById(id);
    }

    @Override
    public boolean checkActiveTrialOfCandidate(Candidate candidate){
        List<Trial> trials=candidate.getTrialEntities();
        if(trials!=null){
            if(!(trials.isEmpty())){
                for(Trial trial:trials){
                    if(trial.getStatus().equals("В процессе")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void checkCandidatesByTestsAndInterview(){
        List<Candidate> candidates=getCandidates();
        if(candidates!=null){
            if(!(candidates.isEmpty())){
                for(Candidate candidate:candidates){
                    List<Trial>trials=candidate.getTrialEntities();
                    if(trials!=null){
                        if(!(trials.isEmpty())){
                            Trial trial=trials.get(0);
                            if(compareDates(new SimpleDateFormat("yyyy-MM-dd").format(trial.getResultTesting().getPositionTest().getDate()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()))<0&&trial.getResultTesting().getPositionTest().getResult().getPoints()==-1){
                                deletePositionQuestions(trial);
                                deleteLanguageQuestions(trial);
                                deleteAllTrial(trial);
                            }
                            if(compareDates(new SimpleDateFormat("yyyy-MM-dd").format(trial.getResultTesting().getLanguageTestEntities().get(0).getDate()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()))<0&&trial.getResultTesting().getLanguageTestEntities().get(0).getResult().getPoints()==-1){
                                deleteLanguageQuestions(trial);
                                deletePositionQuestions(trial);
                                deleteAllTrial(trial);
                            }
                            if(trial.getInterviewEntities()!=null){
                                if(!(trial.getInterviewEntities().isEmpty())){
                                    if(compareDates(new SimpleDateFormat("yyyy-MM-dd").format(trial.getInterviewEntities().get(0).getDateAndTime()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()))<0&&trial.getInterviewEntities().get(0).getResult().getPoints()==-1){
                                        deleteLanguageQuestions(trial);
                                        deletePositionQuestions(trial);
                                        deleteAllTrial(trial);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void deleteCandidateTrial(Trial trial){
        deleteLanguageQuestions(trial);
        deletePositionQuestions(trial);
        deleteAllTrial(trial);
    }

    private void deletePositionQuestions(Trial trial){
        List<PositionTestHasQuestion> positionTestHasQuestions=trial.getResultTesting().getPositionTest().getPositionTestHasQuestionEntities();
        for(PositionTestHasQuestion positionTestHasQuestion:positionTestHasQuestions){
            positionTestHasQuestionService.deletePositionTestHasQuestion(positionTestHasQuestion.getIdPositionTestHasQuestion());
        }
    }

    private void deleteLanguageQuestions(Trial trial){
        List<LanguageTestHasQuestion> languageTestHasQuestions=trial.getResultTesting().getLanguageTestEntities().get(0).getLanguageTestHasQuestionEntities();
        for(LanguageTestHasQuestion languageTestHasQuestion:languageTestHasQuestions){
            languageTestHasQuestionService.deleteLanguageTestHasQuestion(languageTestHasQuestion.getIdLanguageTestHasQuestionEntity());
        }
    }

    private void deleteAllTrial(Trial trial){
        ResultTesting resultTesting=trial.getResultTesting();
        PositionTest positionTest=trial.getResultTesting().getPositionTest();
        LanguageTest languageTest=trial.getResultTesting().getLanguageTestEntities().get(0);
        if(!(trial.getInterviewEntities().isEmpty())){
            Result result=trial.getInterviewEntities().get(0).getResult();
            interviewService.deleteInterview(trial.getInterviewEntities().get(0).getIdInterview());
            resultService.deleteResult(result.getIdResult());
        }
        trial.setResultTesting(null);
        trial.setInterviewEntities(null);
        trialService.addAndUpdateTrial(trial);
        trialService.deleteTrial(trial.getIdTrial());
        Result result1=positionTest.getResult();Result result2=languageTest.getResult();

        languageTestService.deleteLanguageTest(languageTest.getIdLanguageTest());
        resultTestingService.deleteResultTesting(resultTesting.getIdResultTesting());
        positionTestService.deletePositionTest(positionTest.getIdPositionTest());

        resultService.deleteResult(result1.getIdResult());
        resultService.deleteResult(result2.getIdResult());
    }


    public int compareDates(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = format.parse(date1);
            dateTwo = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateOne.compareTo(dateTwo);
    }
}
