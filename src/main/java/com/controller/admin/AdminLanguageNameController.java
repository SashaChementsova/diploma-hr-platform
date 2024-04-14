package com.controller.admin;

import com.model.LanguageName;
import com.service.LanguageNameService;
import com.service.LanguageService;
import com.service.LanguageTestQuestionService;
import com.service.LevelLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminLanguageNameController {
    private final LanguageNameService languageNameService;
    private final LanguageService languageService;

    private final LevelLanguageService levelLanguageService;
    private final LanguageTestQuestionService languageTestQuestionService;
    @Autowired
    public AdminLanguageNameController(LanguageTestQuestionService languageTestQuestionService,LanguageNameService languageNameService, LanguageService languageService, LevelLanguageService levelLanguageService) {
        this.languageNameService = languageNameService;
        this.languageTestQuestionService=languageTestQuestionService;
        this.languageService = languageService;
        this.levelLanguageService = levelLanguageService;
    }

    @GetMapping("/admin/languageNames")
    public String getLanguageNames(Model model){
        checkLanguages();
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        if(languageNameService.getLanguageNames().isEmpty()) model.addAttribute("emptiness","empty");
        return "admin/languageNameControl/getLanguageNames.html";
    }

    @GetMapping("/admin/addLanguageName")
    public String addLanguageName(Model model){
        checkLanguages();
        model.addAttribute("languageName", new LanguageName());
        model.addAttribute("add","add");
        model.addAttribute("languageNames",languageNameService.getLanguageNames());
        if(languageNameService.getLanguageNames().isEmpty()) model.addAttribute("emptiness","empty");
        return "admin/languageNameControl/getLanguageNames.html";
    }

    @PostMapping("/admin/addLanguageName/end")
    public String addLanguageNameEnd(LanguageName languageName, Model model){
        model.addAttribute("languageName", languageName);
        model.addAttribute("add","add");
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        if(languageNameService.getLanguageNames().isEmpty()) model.addAttribute("emptiness","empty");
        if(languageName.getName().isEmpty()||languageName.getName().equals("")){
            model.addAttribute("emptyLanguageName","emptyLanguageName");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        try{
            languageNameService.addAndUpdateLanguageName(languageName);
        }
        catch (Exception ex){
            model.addAttribute("fail","fail");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        return "redirect:/admin/languageNames";
    }

    @GetMapping("/admin/editLanguageName/{id}")
    public String editLanguageName(@PathVariable("id") String idLanguageName, Model model){
        checkLanguages();
        model.addAttribute("edit","edit");
        model.addAttribute("languageName", languageNameService.findLanguageNameById(Integer.parseInt(idLanguageName)));
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        return "admin/languageNameControl/getLanguageNames.html";
    }

    @PostMapping("/admin/editLanguageName/end")
    public String editLanguageNameEnd(LanguageName languageName, Model model){
        model.addAttribute("edit","edit");
        model.addAttribute("languageName", languageName);
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        if(languageName.getName().isEmpty()||languageName.getName().equals("")){
            model.addAttribute("emptyLanguageName","emptyLanguageName");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        try {
            languageNameService.addAndUpdateLanguageName(languageName);
        }
        catch (Exception ex){
            model.addAttribute("fail1","fail1");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        return "redirect:/admin/languageNames";
    }

    @GetMapping("/admin/deleteLanguageName/{id}")
    public String deleteLanguageName(@PathVariable("id")String idLanguageName, Model model){
        checkLanguages();
        LanguageName languageName=new LanguageName();
        languageName.setIdLanguageName(Integer.parseInt(idLanguageName));
        model.addAttribute("delete","delete");
        model.addAttribute("languageName", languageName);
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        return "admin/languageNameControl/getLanguageNames.html";
    }

    @PostMapping("/admin/deleteLanguageName/end")
    public String deleteLanguageNameEnd(LanguageName languageName1, Model model){
        checkLanguages();
        model.addAttribute("delete","delete");
        model.addAttribute("languageName", languageName1);
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        LanguageName languageName=languageNameService.findLanguageNameById(languageName1.getIdLanguageName());
        if(!(languageName.getName().equals(languageName1.getName()))){
            model.addAttribute("notEqual","notEqual");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        if(!(languageNameService.checkLanguageNameByEmployees(languageName))){
            model.addAttribute("notEmptyEmployee","notEmptyEmployee");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        if(!(languageNameService.checkLanguageNameByVacancies(languageName))){
            model.addAttribute("notEmptyVacancy","notEmptyVacancy");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        if(!(languageNameService.checkLanguageNameByCandidate(languageName))){
            model.addAttribute("notEmptyCandidate","notEmptyCandidate");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        if(!(languageNameService.checkLanguageNameByTestQuestions(languageName))){
            model.addAttribute("notEmptyTestQuestions","notEmptyTestQuestions");
            return "admin/languageNameControl/getLanguageNames.html";
        }
        languageTestQuestionService.deleteQuestionsByLanguageName(languageName);
        languageService.deleteLanguageByLanguageName(languageName);
        languageNameService.deleteLanguageName(languageName.getIdLanguageName());
        return "redirect:/admin/languageNames";
    }

    private void checkLanguages(){
        if(languageNameService.getLanguageNames().isEmpty()) languageNameService.initializeLanguageName();
        if(levelLanguageService.getLevelLanguages().isEmpty()) levelLanguageService.initializeLevelLanguage();
    }
}
