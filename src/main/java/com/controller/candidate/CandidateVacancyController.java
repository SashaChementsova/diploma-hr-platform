package com.controller.candidate;

import com.model.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class CandidateVacancyController {
    private final CandidateService candidateService;
    private final VacancyService vacancyService;
    private final TrialService trialService;
    private final PositionTestQuestionService positionTestQuestionService;
    private final LanguageTestQuestionService languageTestQuestionService;
    private final LanguageTestHasQuestionService languageTestHasQuestionService;
    private final PositionTestHasQuestionService positionTestHasQuestionService;
    private final PositionTestService positionTestService;
    private final LanguageTestService languageTestService;
    private final PositionNameService positionNameService;
    private final LevelPositionService levelPositionService;
    private final PositionService positionService;
    private final UserDetailService userDetailService;
    private final ResultTestingService resultTestingService;

    private final ResultService resultService;
    @Autowired
    public CandidateVacancyController(ResultService resultService,ResultTestingService resultTestingService,UserDetailService userDetailService,CandidateService candidateService, VacancyService vacancyService, TrialService trialService, PositionTestQuestionService positionTestQuestionService, LanguageTestQuestionService languageTestQuestionService, LanguageTestHasQuestionService languageTestHasQuestionService, PositionTestHasQuestionService positionTestHasQuestionService, PositionTestService positionTestService, LanguageTestService languageTestService, PositionNameService positionNameService, LevelPositionService levelPositionService, PositionService positionService) {
        this.candidateService = candidateService;
        this.vacancyService = vacancyService;
        this.trialService = trialService;
        this.positionTestQuestionService = positionTestQuestionService;
        this.languageTestQuestionService = languageTestQuestionService;
        this.languageTestHasQuestionService = languageTestHasQuestionService;
        this.positionTestHasQuestionService = positionTestHasQuestionService;
        this.positionTestService = positionTestService;
        this.languageTestService = languageTestService;
        this.positionNameService = positionNameService;
        this.levelPositionService = levelPositionService;
        this.positionService = positionService;
        this.userDetailService=userDetailService;
        this.resultTestingService=resultTestingService;
        this.resultService=resultService;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/candidate/vacancies")
    public String getVacancies(PositionName positionName, LevelPosition levelPosition, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        if(userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().isEmpty()||userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().size()==0){
            model.addAttribute("trialSize","trialSize");
        }
        if (positionName.getIdPositionName()!=0 && levelPosition.getIdLevelPosition()!=0) {
            model.addAttribute("positionNameFind", positionName);
            model.addAttribute("levelPositionFind", levelPosition);
            Position position=new Position();
            position.setLevelPosition(levelPosition); position.setPositionName(positionName);
            position=positionService.addAndUpdatePosition(positionService.checkDuplicatePosition(position));
            model.addAttribute("vacancies", vacancyService.getActiveVacanciesByPosition(position));
            if ( vacancyService.findVacanciesByPosition(position).isEmpty())
                model.addAttribute("emptiness", "empty");
        } else {
            model.addAttribute("positionNameFind", new PositionName());
            model.addAttribute("levelPositionFind", new LevelPosition());
            model.addAttribute("vacancies", vacancyService.getActiveVacancies());
            if (vacancyService.getActiveVacancies().isEmpty())
                model.addAttribute("emptiness", "empty");
        }
        return "candidate/vacancyControl/getVacancies.html";
    }

    @GetMapping("/candidate/vacancy/{id}")
    public String getVacancy(@PathVariable("id") String idVacancy, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        return "candidate/vacancyControl/getVacancy.html";
    }

    @GetMapping("/candidate/applyForVacancy/{id}")
    public String applyForVacancy(@PathVariable("id") String idVacancy, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        if(userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().isEmpty()||userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().size()==0){
            model.addAttribute("trialSize","trialSize");
        }
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("vacancies", vacancyService.getActiveVacancies());
        if (vacancyService.getVacancies().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("applyForVacancy","applyForVacancy");
        model.addAttribute("idVacancy",idVacancy);
        return "candidate/vacancyControl/getVacancies.html";
    }
    @PostMapping("/candidate/applyForVacancyEnd/{id}")
    public String applyForVacancyEnd(@PathVariable("id") String idVacancy, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        Candidate candidate=userDetailService.findUserByEmail(getCurrentUsername()).getCandidate();
        Vacancy vacancy=vacancyService.findVacancyById(Integer.parseInt(idVacancy));
        Trial trial =new Trial();
        trial.setCandidate(candidate);
        trial.setVacancy(vacancy);
        trial.setStatus("В процессе");
        Result result1=new Result();result1.setPoints(-1);result1=resultService.addAndUpdateResult(result1);
        Result result2=new Result();result2.setPoints(-1);result2=resultService.addAndUpdateResult(result2);
        List<PositionTestQuestion> positionTestQuestionList = positionTestQuestionService.generateQuestionsForTest(vacancy.getPosition());
        List<LanguageTestQuestion> languageTestQuestionList=languageTestQuestionService.generateQuestionsForTest(vacancy.getLanguages().get(0));
        PositionTest positionTest=new PositionTest();
        LanguageTest languageTest=new LanguageTest();
        positionTest.setDate(convert(getDateInDays(3))); positionTest.setResult(result1);
        languageTest.setDate(convert(getDateInDays(4))); languageTest.setResult(result2);
        positionTest=positionTestService.addAndUpdatePositionTest(positionTest); languageTest=languageTestService.addAndUpdateLanguageTest(languageTest);
        result1.setPositionTest(positionTest);resultService.addAndUpdateResult(result1);
        positionTestHasQuestionService.createTesting(positionTest,positionTestQuestionList); languageTestHasQuestionService.createTesting(languageTest,languageTestQuestionList);
        ResultTesting resultTesting=new ResultTesting();
        List<LanguageTest> languageTests=new ArrayList<>();languageTests.add(languageTest);
        resultTesting.setPositionTest(positionTest); resultTesting.setLanguageTestEntities(languageTests);
        resultTesting=resultTestingService.addAndUpdateResultTesting(resultTesting);
        positionTest.setResultTesting(resultTesting);
        languageTest.setResultTesting(resultTesting);
        positionTestService.addAndUpdatePositionTest(positionTest);
        languageTestService.addAndUpdateLanguageTest(languageTest);
        trial.setResultTesting(resultTesting);
        trialService.addAndUpdateTrial(trial);
        return "redirect:/candidate/vacancies";
    }

    private java.sql.Date convert(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

    private java.util.Date getDateInDays(int day){
        Date date = new Date (); // Получаем время
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add (calendar.DATE, day); //увеличиваем дату на day
        date = calendar.getTime();
        return date;
    }


}
