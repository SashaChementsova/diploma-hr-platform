package com.controller.hr;

import com.dto.SkillList;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Controller
public class HrVacancyController {
    private final VacancyService vacancyService;
    private final PositionNameService positionNameService;
    private final LevelPositionService levelPositionService;
    private final PositionService positionService;
    private final HrService hrService;
    private final CandidateService candidateService;
    private final BackgroundService backgroundService;
    private final LanguageService languageService;
    private final LanguageNameService languageNameService;
    private final LevelLanguageService levelLanguageService;
    private final CityCompanyService cityCompanyService;
    private final LanguageTestQuestionService languageTestQuestionService;
    private final PositionTestQuestionService positionTestQuestionService;
    private final SkillService skillService;
    private final UserDetailService userDetailService;
    private final TrialService trialService;
    private final EmployeeService employeeService;
    private final InterviewService interviewService;
    private final ResultService resultService;
    @Autowired
    public HrVacancyController(InterviewService interviewService,ResultService resultService,EmployeeService employeeService,TrialService trialService,SkillService skillService,UserDetailService userDetailService,LanguageTestQuestionService languageTestQuestionService,PositionTestQuestionService positionTestQuestionService,VacancyService vacancyService, PositionNameService positionNameService, LevelPositionService levelPositionService, PositionService positionService, HrService hrService, CandidateService candidateService,  BackgroundService backgroundService, LanguageService languageService, LanguageNameService languageNameService, LevelLanguageService levelLanguageService, CityCompanyService cityCompanyService) {
        this.vacancyService = vacancyService;
        this.skillService=skillService;
        this.positionNameService = positionNameService;
        this.levelPositionService = levelPositionService;
        this.positionService = positionService;
        this.hrService = hrService;
        this.candidateService = candidateService;
        this.languageTestQuestionService=languageTestQuestionService;
        this.positionTestQuestionService=positionTestQuestionService;
        this.backgroundService = backgroundService;
        this.languageService = languageService;
        this.languageNameService = languageNameService;
        this.levelLanguageService = levelLanguageService;
        this.cityCompanyService = cityCompanyService;
        this.userDetailService=userDetailService;
        this.trialService=trialService;
        this.employeeService=employeeService;
        this.resultService=resultService;
        this.interviewService=interviewService;
    }


    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/hr/vacancies")
    public String getVacancies(PositionName positionName, LevelPosition levelPosition, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        List<Vacancy> vacancies=hrService.findHrByUserDetail(userDetailService.findUserByEmail(getCurrentUsername())).getVacancyEntities();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        if (positionName.getIdPositionName()!=0 && levelPosition.getIdLevelPosition()!=0) {
            model.addAttribute("positionNameFind", positionName);
            model.addAttribute("levelPositionFind", levelPosition);
            Position position=new Position();
            position.setLevelPosition(levelPosition); position.setPositionName(positionName);
            position=positionService.addAndUpdatePosition(positionService.checkDuplicatePosition(position));
            model.addAttribute("vacancies", vacancyService.findHrVacanciesByPosition(vacancies,position));
            if ( vacancyService.findVacanciesByPosition(position).isEmpty())
                model.addAttribute("emptiness", "empty");
        } else {
            model.addAttribute("positionNameFind", new PositionName());
            model.addAttribute("levelPositionFind", new LevelPosition());
            model.addAttribute("vacancies", vacancies);
            if (vacancyService.getVacancies().isEmpty())
                model.addAttribute("emptiness", "empty");
        }
        return "hr/vacancyControl/getVacancies.html";
    }

    @GetMapping("/hr/vacancy/{id}")
    public String getVacancy(@PathVariable("id") String idVacancy, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        model.addAttribute("trials",trialService.getActiveTrial(vacancyService.findVacancyById(Integer.parseInt(idVacancy))));
        if(checkReady(vacancyService.findVacancyById(Integer.parseInt(idVacancy)))){
            model.addAttribute("finish","finish");
        }
        if(trialService.getActiveTrial(vacancyService.findVacancyById(Integer.parseInt(idVacancy))).isEmpty()) model.addAttribute("emptiness","empty");
        return "hr/vacancyControl/getVacancy.html";
    }

    @GetMapping("/hr/addInterview/{id}")
    public String addInterview(@PathVariable("id") String idTrial,Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        Trial trial =trialService.findTrialById(Integer.parseInt(idTrial));
        ResultTesting resultTesting=trial.getResultTesting();
        model.addAttribute("idVacancy",trial.getVacancy().getIdVacancy());
        if(resultTesting.getPositionTest().getResult().getPoints()==-1||resultTesting.getLanguageTestEntities().get(0).getResult().getPoints()==-1){
            model.addAttribute("notReadyCandidate","notReadyCandidate");
        }
        else{
            model.addAttribute("readyCandidate","readyCandidate");
            model.addAttribute("interview",new Interview());
            model.addAttribute("idTrial",idTrial);
            model.addAttribute("employees",employeeService.getEmployeesUnderPosition(trial.getVacancy().getPosition()));
            model.addAttribute("employee",new Employee());
        }
        return "hr/vacancyControl/addInterview.html";
    }


    @GetMapping("/hr/deleteCandidateFromVacancy/{id}")
    public String deleteCandidate(@PathVariable("id") String idTrial,Model model){
        //candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        Trial trial =trialService.findTrialById(Integer.parseInt(idTrial));
        model.addAttribute("deleteTrial","deleteTrial");
        model.addAttribute("vacancy",trial.getVacancy());
        model.addAttribute("person",trial.getCandidate().getUserDetail().getSNP());
        model.addAttribute("idTrial",idTrial);
        return "hr/vacancyControl/getVacancy.html";
    }

    @PostMapping("/hr/deleteCandidateFromVacancyEnd/{id}")
    public String deleteCandidateEnd(@PathVariable("id") String idTrial,Model model){
//        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        Trial trial =trialService.findTrialById(Integer.parseInt(idTrial));
        trial.setStatus("Отклонено");
        trialService.addAndUpdateTrial(trial);
        return "redirect:/hr/vacancy/"+trial.getVacancy().getIdVacancy();
    }

    @PostMapping("/hr/addInterviewEnd/{id}")
    public String addInterviewEnd(@PathVariable("id") String idTrial,Interview interview,Employee employee,Model model){
        Trial trial =trialService.findTrialById(Integer.parseInt(idTrial));
        model.addAttribute("idVacancy",trial.getVacancy().getIdVacancy());
        model.addAttribute("readyCandidate","readyCandidate");
        model.addAttribute("interview",interview);
        model.addAttribute("idTrial",idTrial);
        model.addAttribute("employees",employeeService.getEmployeesUnderPosition(trial.getVacancy().getPosition()));
        model.addAttribute("employee",employee);
        if(interview.getDateAndTime()==null||interview.getReference().isEmpty()||interview.getReference()==""){
           model.addAttribute("emptyInfo","emptyInfo");
            return "hr/vacancyControl/addInterview.html";
        }
        int res3=employeeService.compareDates(new SimpleDateFormat("yyyy-MM-dd").format(interview.getDateAndTime()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        if(res3<0){
            model.addAttribute("failDate","failDate");
            return "hr/vacancyControl/addInterview.html";
        }
        employee=employeeService.findEmployeeById(employee.getIdEmployee());
        interview.setTrial(trial);
        interview.setEmployee(employee);
        Result result=new Result();
        result.setPoints(-1);
        result=resultService.addAndUpdateResult(result);
        interview.setResult(result);
        interview=interviewService.addAndUpdateInterview(interview);
        List<Interview> interviews=new ArrayList<>();interviews.add(interview);
        employee.setInterviewEntities(interviews);
        employeeService.addAndUpdateEmployee(employee);
        trial.setInterviewEntities(interviews);
        trialService.addAndUpdateTrial(trial);
        return "redirect:/hr/vacancy/"+trial.getVacancy().getIdVacancy();
    }

    @GetMapping("/hr/positionTest/{idVacancy}/{idTrial}")
    public String getLanguageTesting(@PathVariable("idVacancy") String idVacancy,@PathVariable("idTrial") String idTrial, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        model.addAttribute("idVacancy",idVacancy);
        model.addAttribute("candidate",trialService.findTrialById(Integer.parseInt(idTrial)).getCandidate());
        model.addAttribute("test",trialService.findTrialById(Integer.parseInt(idTrial)).getResultTesting().getLanguageTestEntities().get(0));
        if(trialService.findTrialById(Integer.parseInt(idTrial)).getResultTesting().getLanguageTestEntities().get(0).getResult().getPoints()!=-1){
            model.addAttribute("trueInterview","trueInterview");
        }
        return "hr/vacancyControl/getLanguageTest.html";
    }

    @GetMapping("/hr/languageTest/{idVacancy}/{idTrial}")
    public String getPositionTesting(@PathVariable("idVacancy") String idVacancy,@PathVariable("idTrial") String idTrial, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        model.addAttribute("idVacancy",idVacancy);
        model.addAttribute("candidate",trialService.findTrialById(Integer.parseInt(idTrial)).getCandidate());
        model.addAttribute("test",trialService.findTrialById(Integer.parseInt(idTrial)).getResultTesting().getPositionTest());
        if(trialService.findTrialById(Integer.parseInt(idTrial)).getResultTesting().getPositionTest().getResult().getPoints()!=-1){
            model.addAttribute("trueInterview","trueInterview");
        }
        return "hr/vacancyControl/getPositionTest.html";
    }

    @GetMapping("/hr/interview/{idVacancy}/{idTrial}")
    public String getInterview(@PathVariable("idVacancy") String idVacancy,@PathVariable("idTrial") String idTrial, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        model.addAttribute("idVacancy",idVacancy);
        model.addAttribute("candidate",trialService.findTrialById(Integer.parseInt(idTrial)).getCandidate());
        model.addAttribute("employee",trialService.findTrialById(Integer.parseInt(idTrial)).getInterviewEntities().get(0).getEmployee());
        model.addAttribute("interview",trialService.findTrialById(Integer.parseInt(idTrial)).getInterviewEntities().get(0));
        if(trialService.findTrialById(Integer.parseInt(idTrial)).getInterviewEntities().get(0).getResult()!=null){
            if(trialService.findTrialById(Integer.parseInt(idTrial)).getInterviewEntities().get(0).getResult().getPoints()!=-1){
                model.addAttribute("trueInterview","trueInterview");
            }
        }
        return "hr/vacancyControl/getInterview.html";
    }

    @GetMapping("/hr/getCandidateProfile/{idUser}")
    public String getCandidateByVacancy(@PathVariable("idUser") String idUser, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        model.addAttribute("candidate",userDetailService.findUserById(Integer.parseInt(idUser)).getCandidate());
        return "hr/vacancyControl/getCandidate.html";
    }

    @GetMapping("/hr/getEmployeeProfile/{idUser}")
    public String getEmployeeByInterview(@PathVariable("idUser") String idUser, Model model) {
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        model.addAttribute("employee",userDetailService.findUserById(Integer.parseInt(idUser)).getEmployee());
        return "hr/vacancyControl/getEmployee.html";
    }



    @GetMapping("/hr/addVacancy")
    public String addVacancy(Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        model.addAttribute("vacancy",new Vacancy());
        model.addAttribute("positionName",new PositionName());
        model.addAttribute("levelPosition",new LevelPosition());
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("languageNameAdd",new LanguageName());
        model.addAttribute("levelLanguageAdd",new LevelLanguage());
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("background",new Background());
        model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
        model.addAttribute("cityCompany",new CityCompany());
        SkillList skillList=new SkillList();int size;
        if(skillService.getSkills().size()<5) size=skillService.getSkills().size();
        else size=5;
        for(int i=0;i<size;i++){
            skillList.addSkill(new Skill());
        }
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("skillL", skillList);
        return "hr/vacancyControl/addVacancy.html";
    }
    @PostMapping("/hr/addVacancy/end")
    public String addVacancyEnd(SkillList skillList,Vacancy vacancy, PositionName positionName, LevelPosition levelPosition, LanguageName languageName, LevelLanguage levelLanguage, Background background, CityCompany cityCompany, Model model){
        model.addAttribute("vacancy",vacancy);
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("skillL", skillList);
        model.addAttribute("positionName",positionName);
        model.addAttribute("levelPosition",levelPosition);
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("languageNameAdd",languageName);
        model.addAttribute("levelLanguageAdd",levelLanguage);
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("background",background);
        model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
        model.addAttribute("cityCompany",cityCompany);
        Position position=new Position();
        position.setPositionName(positionName);
        position.setLevelPosition(levelPosition);
        position=positionService.checkDuplicatePosition(position);
        positionService.addAndUpdatePosition(position);
        if(!(positionTestQuestionService.checkNumOfQuestionsByPosition(position,10))){
            model.addAttribute("failPositionQuestions","failPositionQuestions");
            return "hr/vacancyControl/addVacancy.html";
        }
        Language language=new Language();
        language.setLanguageName(languageName);
        language.setLevelLanguage(levelLanguage);
        language=languageService.checkDuplicateLanguage(language);
        languageService.addAndUpdateLanguage(language);
        if(!(languageTestQuestionService.checkNumOfQuestionsByLanguage(language,10))){
            model.addAttribute("failLanguageQuestions","failLanguageQuestions");
            return "hr/vacancyControl/addVacancy.html";
        }
        List<Skill> skills=new ArrayList<>();
        for(Skill skill:skillList.getSkillList()){
            if(skill.getIdSkill()!=0){
                skills.add(skill);
            }
        }
        background.setSkills(skills);
        vacancy.setPosition(position);
        List<Language> languages=new ArrayList<>();languages.add(language);
        vacancy.setLanguages(languages);
        background=backgroundService.addAndUpdateBackground(background);
        vacancy.setBackground(background);
        vacancy.setCityCompany(cityCompanyService.findCityCompanyById(cityCompany.getIdCityCompany()));
        vacancy.setStatus("В процессе");
        vacancy.setHr(hrService.findHrByUserDetail(userDetailService.findUserByEmail(getCurrentUsername())));
        vacancyService.addAndUpdateVacancy(vacancy);
        return "redirect:/hr/vacancies";
    }

    @GetMapping("/hr/endVacancy/{id}")
    public String endVacancy(@PathVariable("id") String idVacancy, Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        List<Vacancy> vacancies=hrService.findHrByUserDetail(userDetailService.findUserByEmail(getCurrentUsername())).getVacancyEntities();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("vacancies", vacancies);
        if (vacancyService.getVacancies().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("idVacancy",idVacancy);
        model.addAttribute("endVacancy","endVacancy");
        return "hr/vacancyControl/getVacancies.html";
    }

    @PostMapping("/hr/endVacancyEnd/{id}")
    public String endVacancyEnd(@PathVariable("id") String idVacancy, Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        checkPositions();
        Vacancy vacancy=vacancyService.findVacancyById(Integer.parseInt(idVacancy));
        if(vacancy.getTrialEntities().isEmpty()){
            vacancyService.deleteVacancy(vacancy.getIdVacancy());
        }
        else{
            vacancy.setStatus("Завершено");
            vacancyService.addAndUpdateVacancy(vacancy);
        }
        return "redirect:/hr/vacancies";
    }


    private void checkPositions(){
        if(positionNameService.getPositionNames().isEmpty()) positionNameService.initializePositionName();
        if(levelPositionService.getLevelPositions().isEmpty()) levelPositionService.initializeLevelPosition();
    }

    private boolean checkReady(Vacancy vacancy){
        List<Trial> trials=vacancy.getTrialEntities();
        if(!(vacancy.getStatus().equals("Завершено"))) return false;
        if(trials!=null){
            if(!(trials.isEmpty())){
                for(Trial trial:trials){
                    if(trial.getResultTesting().getPositionTest().getResult().getPoints()==-1||trial.getResultTesting().getLanguageTestEntities().get(0).getResult().getPoints()==-1){
                        return false;
                    }
                    if(trial.getInterviewEntities().isEmpty()) return false;
                    if(trial.getInterviewEntities().get(0).getResult().getPoints()==-1){
                        return false;
                    }
                }
            }
            else return false;
        }
        else return false;
        return true;
    }
}
