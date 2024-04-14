package com.controller.hr;

import com.comparators.CandidateFinalComparator;
import com.dto.CandidatesFinals;
import com.dto.UserDto;
import com.dto.UsersDto;
import com.model.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HrDecisionController {
    private final EmployeeService employeeService;
    private final UserDetailService userDetailService;
    private final RoleService roleService;
    private final HrService hrService;
    private final VacancyService vacancyService;
    private final TrialService trialService;

    private final LanguageService languageService;
    private final EducationService educationService;

    private final CandidateService candidateService;
    @Autowired
    public HrDecisionController(LanguageService languageService,EducationService educationService,CandidateService candidateService, EmployeeService employeeService, UserDetailService userDetailService, RoleService roleService, HrService hrService, VacancyService vacancyService, TrialService trialService) {
        this.languageService=languageService;
        this.employeeService = employeeService;
        this.userDetailService = userDetailService;
        this.roleService = roleService;
        this.hrService = hrService;
        this.vacancyService = vacancyService;
        this.trialService = trialService;
        this.candidateService=candidateService;
        this.educationService=educationService;
    }

    @GetMapping("/hr/getEmployeesFromCandidate/{id}")
    public String finishVacancy(@PathVariable("id") String idVacancy, Model model){
        UsersDto usersDto=new UsersDto();
        List<Trial> trials=trialService.getActiveTrial(vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        UserDto dateOfEndContract=new UserDto();
        List<CandidatesFinals> candidatesFinals=new ArrayList<>();
        List<UserDto> userDtos=new ArrayList<>();
        for(int i=0;i<trials.size();i++){
            UserDto userDto=new UserDto();
            userDto.setId(0);
            userDtos.add(userDto);
        }
        usersDto.setUsers(userDtos);
        System.out.println("888888888888888888888888       "+usersDto.getUsers().size());
        model.addAttribute("candidates",setCandidatesFinals(candidatesFinals,trials));
        model.addAttribute("users",usersDto);
        model.addAttribute("dateOfEndContract",dateOfEndContract);
        model.addAttribute("idVacancy",idVacancy);
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        return "hr/vacancyControl/finishDecision.html";
    }

    @PostMapping("/hr/getEmployeesFromCandidateEnd/{id}")
    public String finishVacancyEnd(@PathVariable("id") String idVacancy,UsersDto usersDto,UserDto dateOfEndContract, Model model){
        List<CandidatesFinals> candidatesFinals=new ArrayList<>();
        model.addAttribute("candidates",setCandidatesFinals(candidatesFinals,trialService.getActiveTrial(vacancyService.findVacancyById(Integer.parseInt(idVacancy)))));
        model.addAttribute("users",usersDto);
        model.addAttribute("dateOfEndContract",dateOfEndContract);
        model.addAttribute("idVacancy",idVacancy);
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        Vacancy vacancy=vacancyService.findVacancyById(Integer.parseInt(idVacancy));
        if(getSizeOfUsers(usersDto)>vacancy.getFreePositions()){
            model.addAttribute("tooMuch");
            return "hr/vacancyControl/finishDecision.html";
        }
        if(calculateDifferenceDates(dateOfEndContract)>5||calculateDifferenceDates(dateOfEndContract)<1){
            model.addAttribute("dateFail");
            return "hr/vacancyControl/finishDecision.html";
        }
        System.out.println(usersDto.getUsers().get(0).getId());
        for(UserDto userDto:usersDto.getUsers()){
            if(userDto.getId()!=0){
                UserDetail userDetail=userDetailService.findUserById(userDto.getId());
                Employee employee=new Employee();
                employee.setUserDetail(userDetail);
                employee.setDateRecruitment(convert(new java.util.Date()));
                employee.setDateContractEnd(dateOfEndContract.getBirthday());
                employee.setPosition(vacancy.getPosition());
                employee.setCityCompany(vacancy.getCityCompany());
                employee.setBackground(userDetail.getCandidate().getBackground());
                Candidate candidate=userDetail.getCandidate();
                employee=employeeService.addAndUpdateEmployee(employee);
                for(Education education:candidate.getEducationEntities()){
                    education.setCandidate(null);
                    education.setEmployee(employee);
                    educationService.addAndUpdateEducation(education);
                }
                List<Language> languages=candidate.getLanguages();
                if(languages!=null&&!(languages.isEmpty())){
                    Language language=languages.get(0);
                    List<Candidate> candidates=language.getCandidateEntities();
                    candidates.remove(candidate);
                    language.setCandidateEntities(candidates);
                    List<Employee> employees=language.getEmployeeEntities();
                    employees.add(employee);
                    language.setEmployeeEntities(employees);
                    languageService.addAndUpdateLanguage(language);
                }
                employee=employeeService.addAndUpdateEmployee(employee);
                userDetail.setCandidate(null); userDetail.setEmployee(employee);
                List<Role> roles=new ArrayList<>();roles.add(roleService.findRoleById(3));
                if(employee.getPosition().getPositionName().getName().equals("HR-менеджер")){
                    Hr hr=new Hr();
                    hr.setUserDetail(userDetail);
                    roles.add(roleService.findRoleById(4));
                    hr=hrService.addAndUpdateHr(hr);
                    userDetail.setHr(hr);
                }
                userDetail.setRoles(roles);
                userDetailService.updateUser(userDetail);
                candidate.setBackground(null);
                candidateService.addAndUpdateCandidate(candidate);
                List<Trial> trials=candidate.getTrialEntities();
                for(Trial trial:trials) {
                   candidateService.deleteCandidateTrial(trial);
                }
                candidateService.deleteCandidate(candidate.getIdCandidate());
            }
            else{
                UserDetail userDetail=userDetailService.findUserById(userDto.getId());
                Trial trial=userDetail.getCandidate().getTrialEntities().get(0);

                candidateService.deleteCandidateTrial(trial);
            }
        }
//        List<Trial> trials=vacancy.getTrialEntities();
//        if(trials!=null){
//            if(!(trials.isEmpty())){
//                for(Trial trial:trials)
//                    candidateService.deleteCandidateTrial(trial);
//            }
//        }
        vacancyService.deleteVacancy(vacancy.getIdVacancy());
        return "redirect:/hr/vacancies";
    }

    private int getSizeOfUsers(UsersDto usersDto){
        int res=0;
        for(UserDto userDto:usersDto.getUsers()){
            System.out.println(userDto.getId());
            if(userDto.getId()!=0){
                res++;
            }
        }
        return res;
    }

    private int calculateDifferenceDates(UserDto userDto){
        int age;
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(userDto.getBirthday());
        String date2 = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = format.parse(date1);
            dateTwo = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Количество дней между датами в миллисекундах
        long difference = dateOne.getTime() - dateTwo.getTime();
        // Перевод количества дней между датами из миллисекунд в дни
        double days = difference / (24 * 60 * 60 * 1000); // миллисекунды / (24ч * 60мин * 60сек * 1000мс)
        double years=days/365;
        age= (int) Math.floor(years);
        System.out.println("ГОДААААААА "+age);
        return age;
    }
    private java.sql.Date convert(java.util.Date uDate) {
        return new java.sql.Date(uDate.getTime());
    }


    private List<CandidatesFinals> setCandidatesFinals(List<CandidatesFinals> candidatesFinals,List<Trial> trials){
        for(Trial trial:trials){
            CandidatesFinals candidatesFinal=new CandidatesFinals();
            candidatesFinal.setIdUser(trial.getCandidate().getUserDetail().getIdUserDetails());
            candidatesFinal.setSNP(trial.getCandidate().getUserDetail().getShortSNP());
            float res=(trial.getInterviewEntities().get(0).getResult().getPoints()+trial.getResultTesting().getLanguageTestEntities().get(0).getResult().getPoints()+trial.getResultTesting().getPositionTest().getResult().getPoints())/3;
            candidatesFinal.setPoints(Math.round(res));
            candidatesFinals.add(candidatesFinal);
        }
        candidatesFinals.sort(new CandidateFinalComparator());
        return candidatesFinals;
    }

}
