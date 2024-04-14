package com.controller.candidate;

import com.dto.Password;
import com.dto.SkillList;
import com.model.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class CandidateProfileController {
    private  final UserDetailService userDetailService;

    private final CandidateService candidateService;
    private final SkillService skillService;
    private final BackgroundService backgroundService;
    private final EducationService educationService;
    private final EducationTypeService educationTypeService;
    private final LanguageService languageService;
    private final LanguageNameService languageNameService;
    private final LevelLanguageService levelLanguageService;
    @Autowired
    public CandidateProfileController(UserDetailService userDetailService,CandidateService candidateService, SkillService skillService, BackgroundService backgroundService, EducationService educationService, EducationTypeService educationTypeService, LanguageService languageService, LanguageNameService languageNameService, LevelLanguageService levelLanguageService) {
        this.userDetailService = userDetailService;
        this.candidateService=candidateService;
        this.skillService = skillService;
        this.backgroundService = backgroundService;
        this.educationService = educationService;
        this.educationTypeService = educationTypeService;
        this.languageService = languageService;
        this.languageNameService = languageNameService;
        this.levelLanguageService = levelLanguageService;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/candidate/getProfile")
    public String getProfileCandidate(Model model){
        Candidate candidate=userDetailService.findUserByEmail(getCurrentUsername()).getCandidate();
        model.addAttribute("candidate",candidate);
        return "candidate/profileControl/getProfile.html";
    }

    @GetMapping("/candidate/editProfile")
    public String editProfileCandidate(Model model){
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        model.addAttribute("user",userDetail);
        model.addAttribute("candidate",userDetail.getCandidate());
        model.addAttribute("languageNames",languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages",levelLanguageService.getLevelLanguages());
        if(!(userDetail.getCandidate().getLanguages().isEmpty())){
            model.addAttribute("languageNameEdit",userDetail.getCandidate().getLanguages().get(0).getLanguageName());
            model.addAttribute("levelLanguageEdit",userDetail.getCandidate().getLanguages().get(0).getLevelLanguage());
        }
        else{
            model.addAttribute("languageNameEdit",new LanguageName());
            model.addAttribute("levelLanguageEdit",new LevelLanguage());
        }
        if(userDetail.getCandidate().getEducationEntities().isEmpty()){
            Education education=new Education();
            model.addAttribute("educationEdit",education);
            model.addAttribute("educationTypeEdit",new EducationType());
        }
        else{
            model.addAttribute("educationEdit",userDetail.getCandidate().getEducationEntities().get(0));
            model.addAttribute("educationTypeEdit",userDetail.getCandidate().getEducationEntities().get(0).getEducationType());
        }
        model.addAttribute("educationTypes",educationTypeService.getEducationTypes());
        model.addAttribute("background",userDetail.getCandidate().getBackground());
        SkillList skillList=new SkillList();int size;
        if(!(userDetail.getCandidate().getBackground().getSkills().isEmpty())){
            List<Skill> skills=userDetail.getCandidate().getBackground().getSkills();
            size=skills.size();
            for(int i=0;i<size;i++){
                skillList.addSkill(skills.get(i));
            }
            int size1=5;
            if(skillService.getSkills().size()<5) size1=skillService.getSkills().size();
            if(size<size1) {
                for (int i = size; i < size1; i++) {
                    skillList.addSkill(new Skill());
                }
            }
        }
        else {
            if(skillService.getSkills().size()<5) size=skillService.getSkills().size();
            else size=5;
            for(int i=0;i<size;i++){
                skillList.addSkill(new Skill());
            }
        }
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("skillL", skillList);
        return "candidate/profileControl/editProfile.html";
    }

    @PostMapping("candidate/candidateEditProfileEnd")
    public String editProfileCandidateEnd(UserDetail userDetail, Candidate candidate, LanguageName languageName, LevelLanguage levelLanguage, Education education, EducationType educationType, Background background, SkillList skillList, Model model){
        Candidate candidate1=candidateService.findCandidateById(candidate.getIdCandidate());

        model.addAttribute("user",userDetail);
        model.addAttribute("candidate",candidate);
        model.addAttribute("languageNameEdit",languageName);
        model.addAttribute("languageNames",languageNameService.getLanguageNames());
        model.addAttribute("levelLanguageEdit",levelLanguage);
        model.addAttribute("levelLanguages",levelLanguageService.getLevelLanguages());
        model.addAttribute("educationEdit",education);
        model.addAttribute("educationTypeEdit",educationType);
        model.addAttribute("educationTypes",educationTypeService.getEducationTypes());
        model.addAttribute("background",background);
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("skillL", skillList);
        if(!(checkUserEmptiness(userDetail,model)))  return "candidate/profileControl/editProfile.html";
        if(!(checkEmail(userDetail,model)))  return "candidate/profileControl/editProfile.html";
        if(!(checkUserExisting(userDetail,model)))  return "candidate/profileControl/editProfile.html";
        if(!(checkUserAge(userDetail,model)))  return "candidate/profileControl/editProfile.html";
        Language language=new Language();
        if(!(candidate1.getLanguages().isEmpty()))language=candidateService.findCandidateById(candidate.getIdCandidate()).getLanguages().get(0);
        language.setLevelLanguage(levelLanguage);language.setLanguageName(languageName);
        language=languageService.checkDuplicateLanguage(language);
        language=languageService.addAndUpdateLanguage(language);
        List<Language> languages=new ArrayList<>();languages.add(language);
        candidate.setLanguages(languages);
        education.setEducationType(educationType);
        education.setCandidate(candidate);
        education=educationService.addAndUpdateEducation(education);
        List<Education> educations=new ArrayList<>();educations.add(education);
        candidate.setEducationEntities(educations);
        List<Skill> skills=new ArrayList<>();
        for(Skill skill:skillList.getSkillList()){
            if(skill.getIdSkill()!=0){
                skills.add(skill);
            }
        }
        Background background1=new Background();
        if(candidate1.getBackground()!=null) background1=candidate1.getBackground();
        background1.setExperience(background.getExperience());
        background1.setSkills(skills);
        background1.setCandidate(candidate);
        background1=backgroundService.addAndUpdateBackground(background1);
        candidate.setBackground(background1);
        candidate.setUserDetail(userDetail);
        candidate=candidateService.addAndUpdateCandidate(candidate);
        userDetail.setCandidate(candidate);
        userDetail.setImage(candidate1.getUserDetail().getImage());
        userDetailService.updateUser(userDetail);
        return "redirect:/candidate/getProfile";
    }

    @GetMapping("/candidate/changePassword")
    public String changePasswordCandidate(Model model){
        Password password=new Password();
        password.setIdUser(userDetailService.findUserByEmail(getCurrentUsername()).getIdUserDetails());
        model.addAttribute("password",password);
        return "candidate/profileControl/changePassword.html";
    }

    @PostMapping("/candidate/changePasswordEnd")
    public String changePasswordEnd( Password password,Model model){
        if(checkPasswords(password,model)) return "candidate/profileControl/changePassword.html";
        userDetailService.savePassword(userDetailService.findUserById(password.getIdUser()),password.getNewPassword());
        return "redirect:/candidate/getProfile";
    }

    public boolean checkUserExisting(UserDetail userDetail,Model model){
        UserDetail userDetail1=userDetailService.findUserByEmail(userDetail.getEmail());
        if(userDetail1.getIdUserDetails()!=userDetail.getIdUserDetails()&&(userDetail1.getEmail().equals(userDetail.getEmail())||userDetail1.getPhone().equals(userDetail.getPhone()))){
            model.addAttribute("existingUser","existingUser");
            return false;
        }
        return true;
    }

    public boolean checkUserEmptiness(UserDetail user,Model model){
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        if (user.getPassword().equals("")||user.getName().equals("") || user.getSurname().equals("") ||  dateOfBirth.equals("") || user.getPhone().equals("")){
            model.addAttribute("empty","empty");
            return false;
        }
        return true;
    }

    public boolean checkUserAge(UserDetail user,Model model){
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        int age= userDetailService.calculateAge(dateOfBirth);
        if(age<18) {
            model.addAttribute("age","age");
            return false;
        }
        return true;
    }

    public boolean checkEmail(UserDetail user,Model model){
        final String PASSWORD_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(user.getEmail()).matches())){
            model.addAttribute("wrongEmail","wrongEmail");
            return false;
        }
        return true;
    }

    public boolean checkPasswords(Password password,Model model){
        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
        model.addAttribute("password",password);
        if(password.getNewPassword().isEmpty()||password.getOldPassword().isEmpty()||password.getNewPasswordRepeat().isEmpty()){
            model.addAttribute("empty","empty");
            return true;
        }
//        ^                  # start of the string
//        (?=.*[0-9])        # a digit must occur at least once
//                (?=.*[a-z])        # a lower case letter must occur at least once
//        (?=.*[A-Z])        # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])   # a special character must occur at least once
//        (?=\\S+$)          # no whitespace allowed in the entire string
//                .{8,16}            # 8-16 character password, both inclusive
//        $                  # end of the string
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(password.getOldPassword()).matches())){
            model.addAttribute("wrongOldPassword","wrongOldPassword");
            return true;
        }
        if(!(PASSWORD_PATTERN.matcher(password.getNewPassword()).matches())){
            model.addAttribute("wrongNewPassword","wrongNewPassword");
            return true;
        }
        if(!(userDetailService.checkPassword(userDetailService.findUserById(password.getIdUser()),password.getOldPassword()))){
            model.addAttribute("wrongOldUserPassword","wrongOldUserPassword");
            return true;
        }
        if(!(password.getNewPassword().equals(password.getNewPasswordRepeat()))){
            model.addAttribute("notEqual","notEqual");
            return true;
        }
        return false;
    }
}
