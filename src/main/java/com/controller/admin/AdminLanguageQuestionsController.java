package com.controller.admin;

import com.model.Language;
import com.model.LanguageName;
import com.model.LanguageTestQuestion;
import com.model.LevelLanguage;
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
public class AdminLanguageQuestionsController {
    private final LanguageNameService languageNameService;
    private final LanguageService languageService;
    private final LevelLanguageService levelLanguageService;
    private final LanguageTestQuestionService languageTestQuestionService;
    @Autowired
    public AdminLanguageQuestionsController(LanguageNameService languageNameService, LanguageService languageService, LevelLanguageService levelLanguageService, LanguageTestQuestionService languageTestQuestionService) {
        this.languageNameService = languageNameService;
        this.languageService = languageService;
        this.levelLanguageService = levelLanguageService;
        this.languageTestQuestionService = languageTestQuestionService;
    }

    @GetMapping("/admin/languageQuestions")
    public String getLanguageQuestions(LanguageName languageName,LevelLanguage levelLanguage,Model model) {
        checkLanguages();
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        if (languageName.getIdLanguageName()!=0 && levelLanguage.getIdLevelLanguage()!=0) {
            System.out.println("print "+languageName.getIdLanguageName()+" "+levelLanguage.getIdLevelLanguage());
            model.addAttribute("languageNameFind", languageName);
            model.addAttribute("levelLanguageFind", levelLanguage);
            Language language=new Language();
            language.setLevelLanguage(levelLanguage); language.setLanguageName(languageName);
            System.out.println(language.getLanguageName().getIdLanguageName()+" n "+language.getLevelLanguage().getIdLevelLanguage());
            language=languageService.addAndUpdateLanguage(languageService.checkDuplicateLanguage(language));
            model.addAttribute("languageQuestions", languageTestQuestionService.findLanguageTestQuestionsByLanguage(language));
            if (languageTestQuestionService.findLanguageTestQuestionsByLanguage(language).isEmpty())
                model.addAttribute("emptiness", "empty");
        } else {
            model.addAttribute("languageNameFind", new LanguageName());
            model.addAttribute("levelLanguageFind", new LevelLanguage());
            model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
            if (languageTestQuestionService.getLanguageTestQuestions().isEmpty())
                model.addAttribute("emptiness", "empty");
        }
        return "admin/languageQuestionsControl/getLanguageQuestions.html";
    }

    @GetMapping("/admin/addLanguageQuestion")
    public String addLanguageQuestion(Model model){
        checkLanguages();
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("languageNameFind", new LanguageName());
        model.addAttribute("levelLanguageFind", new LevelLanguage());
        model.addAttribute("languageName",new LanguageName());
        model.addAttribute("levelLanguage",new LevelLanguage());
        model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
        if (languageTestQuestionService.getLanguageTestQuestions().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("languageTestQuestion", new LanguageTestQuestion());
        model.addAttribute("add","add");
       return "admin/languageQuestionsControl/getLanguageQuestions.html";
    }

    @PostMapping("/admin/addLanguageQuestion/end")
    public String addLanguageQuestionEnd(LanguageTestQuestion languageTestQuestion,LanguageName languageName,LevelLanguage levelLanguage, Model model){
        System.out.println("add "+languageName.getIdLanguageName()+" "+levelLanguage.getIdLevelLanguage());
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("languageNameFind", new LanguageName());
        model.addAttribute("levelLanguageFind", new LevelLanguage());
        model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
        if (languageTestQuestionService.getLanguageTestQuestions().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("languageQuestion",languageTestQuestion);
        model.addAttribute("add","add");
        if(!(checkEmptinessLanguageTestQuestion(languageTestQuestion))){
            model.addAttribute("emptyLanguageQuestion","emptyLanguageQuestion");
            return "admin/languageQuestionsControl/getLanguageQuestions.html";
        }
        Language language=new Language();
        language.setLanguageName(languageName);
        language.setLevelLanguage(levelLanguage);
        language=languageService.checkDuplicateLanguage(language);
        languageService.addAndUpdateLanguage(language);
        languageTestQuestion.setLanguage(language);
        languageTestQuestionService.addAndUpdateLanguageTestQuestion(languageTestQuestion);
        return "redirect:/admin/languageQuestions";
    }
    @GetMapping("/admin/editLanguageQuestion/{id}")
    public String editLanguageQuestion(@PathVariable("id") String idLanguageQuestion, Model model){
        checkLanguages();
        LanguageTestQuestion languageTestQuestion=languageTestQuestionService.findLanguageTestQuestionById(Integer.parseInt(idLanguageQuestion));
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("languageNameFind", new LanguageName());
        model.addAttribute("levelLanguageFind", new LevelLanguage());
        model.addAttribute("languageName",languageTestQuestion.getLanguage().getLanguageName());
        model.addAttribute("levelLanguage",languageTestQuestion.getLanguage().getLevelLanguage());
        model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
        model.addAttribute("languageTestQuestion", languageTestQuestion);
        model.addAttribute("edit","edit");
        return "admin/languageQuestionsControl/getLanguageQuestions.html";
    }

    @PostMapping("/admin/editLanguageQuestion/end")
    public String editLanguageQuestionEnd(LanguageTestQuestion languageTestQuestion,LanguageName languageName,LevelLanguage levelLanguage, Model model){
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("languageNameFind", new LanguageName());
        model.addAttribute("levelLanguageFind", new LevelLanguage());
        model.addAttribute("languageName",languageName);
        model.addAttribute("levelLanguage",levelLanguage);
        model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
        model.addAttribute("languageTestQuestion",languageTestQuestion);
        model.addAttribute("edit","edit");
        if(!(checkEmptinessLanguageTestQuestion(languageTestQuestion))){
            model.addAttribute("emptyLanguageQuestion","emptyLanguageQuestion");
            return "admin/languageQuestionsControl/getLanguageQuestions.html";
        }
        System.out.println(languageTestQuestion.getIdLanguageTestQuestion()+" id");
        Language language=new Language();
        language.setLanguageName(languageName);
        language.setLevelLanguage(levelLanguage);
        language=languageService.checkDuplicateLanguage(language);
        languageService.addAndUpdateLanguage(language);
        languageTestQuestion.setLanguage(language);
        languageTestQuestionService.addAndUpdateLanguageTestQuestion(languageTestQuestion);
        return "redirect:/admin/languageQuestions";
    }

    @GetMapping("/admin/deleteLanguageQuestion/{id}")
    public String deleteLanguageQuestion(@PathVariable("id")String idLanguageQuestion, Model model){
        checkLanguages();
        LanguageTestQuestion languageTestQuestion=new LanguageTestQuestion();
        languageTestQuestion.setIdLanguageTestQuestion(Integer.parseInt(idLanguageQuestion));
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("languageNameFind", new LanguageName());
        model.addAttribute("levelLanguageFind", new LevelLanguage());
        model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
        model.addAttribute("languageTestQuestion",languageTestQuestion);
        model.addAttribute("delete","delete");
        return "admin/languageQuestionsControl/getLanguageQuestions.html";
    }

    @PostMapping("/admin/deleteLanguageQuestion/end")
    public String deleteLanguageQuestionEnd(LanguageTestQuestion languageTestQuestion1, Model model){
        checkLanguages();
        model.addAttribute("languageNames", languageNameService.getLanguageNames());
        model.addAttribute("levelLanguages", levelLanguageService.getLevelLanguages());
        model.addAttribute("languageNameFind", new LanguageName());
        model.addAttribute("levelLanguageFind", new LevelLanguage());
        model.addAttribute("languageQuestions", languageTestQuestionService.getLanguageTestQuestions());
        model.addAttribute("languageTestQuestion",languageTestQuestion1);
        model.addAttribute("delete","delete");
        LanguageTestQuestion languageTestQuestion=languageTestQuestionService.findLanguageTestQuestionById(languageTestQuestion1.getIdLanguageTestQuestion());
        if(!(languageTestQuestionService.checkDateOfLanguageTestByQuestion(languageTestQuestion))){
            model.addAttribute("notEmptyTestQuestions","notEmptyTestQuestions");
            return "admin/languageQuestionsControl/getLanguageQuestions.html";
        }
        languageTestQuestionService.deleteQuestionFromTestHasQuestions(languageTestQuestion);
        return "redirect:/admin/languageQuestions";
    }

    private boolean checkEmptinessLanguageTestQuestion(LanguageTestQuestion languageTestQuestion){
        if(languageTestQuestion.getQuestion().isEmpty()||languageTestQuestion.getQuestion().equals("")){
            return false;
        }
        if(languageTestQuestion.getRightAnswer().isEmpty()||languageTestQuestion.getRightAnswer().equals("")){
            return false;
        }
        if(languageTestQuestion.getAnswer2().isEmpty()||languageTestQuestion.getAnswer2().equals("")){
            return false;
        }
        if(languageTestQuestion.getAnswer3().isEmpty()||languageTestQuestion.getAnswer3().equals("")){
            return false;
        }
        if(languageTestQuestion.getAnswer4().isEmpty()||languageTestQuestion.getAnswer4().equals("")){
            return false;
        }
        if(languageTestQuestion.getPoint()==0){
            return false;
        }
        return true;
    }


    private void checkLanguages(){
        if(languageNameService.getLanguageNames().isEmpty()) languageNameService.initializeLanguageName();
        if(levelLanguageService.getLevelLanguages().isEmpty()) levelLanguageService.initializeLevelLanguage();
    }
}
