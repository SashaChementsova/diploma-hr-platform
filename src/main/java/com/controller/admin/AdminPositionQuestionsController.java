package com.controller.admin;

import com.model.*;
import com.service.PositionNameService;
import com.service.PositionService;
import com.service.PositionTestQuestionService;
import com.service.LevelPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminPositionQuestionsController {
    private final PositionNameService positionNameService;
    private final PositionService positionService;
    private final LevelPositionService levelPositionService;
    private final PositionTestQuestionService positionTestQuestionService;
    @Autowired
    public AdminPositionQuestionsController(PositionNameService positionNameService, PositionService positionService, LevelPositionService levelPositionService, PositionTestQuestionService positionTestQuestionService) {
        this.positionNameService = positionNameService;
        this.positionService = positionService;
        this.levelPositionService = levelPositionService;
        this.positionTestQuestionService = positionTestQuestionService;
    }

    @GetMapping("/admin/positionQuestions")
    public String getPositionQuestions(PositionName positionName,LevelPosition levelPosition,Model model) {
        checkPositions();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        if (positionName.getIdPositionName()!=0 && levelPosition.getIdLevelPosition()!=0) {
            System.out.println("print "+positionName.getIdPositionName()+" "+levelPosition.getIdLevelPosition());
            model.addAttribute("positionNameFind", positionName);
            model.addAttribute("levelPositionFind", levelPosition);
            Position position=new Position();
            position.setLevelPosition(levelPosition); position.setPositionName(positionName);
            System.out.println(position.getPositionName().getIdPositionName()+" n "+position.getLevelPosition().getIdLevelPosition());
            position=positionService.addAndUpdatePosition(positionService.checkDuplicatePosition(position));
            model.addAttribute("positionQuestions", positionTestQuestionService.findPositionTestQuestionsByPosition(position));
            if (positionTestQuestionService.findPositionTestQuestionsByPosition(position).isEmpty())
                model.addAttribute("emptiness", "empty");
        } else {
            model.addAttribute("positionNameFind", new PositionName());
            model.addAttribute("levelPositionFind", new LevelPosition());
            model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
            if (positionTestQuestionService.getPositionTestQuestions().isEmpty())
                model.addAttribute("emptiness", "empty");
        }
        return "admin/positionQuestionsControl/getPositionQuestions.html";
    }

    @GetMapping("/admin/addPositionQuestion")
    public String addPositionQuestion(Model model){
        checkPositions();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionName",new PositionName());
        model.addAttribute("levelPosition",new LevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
        if (positionTestQuestionService.getPositionTestQuestions().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("positionTestQuestion", new PositionTestQuestion());
        model.addAttribute("add","add");
        return "admin/positionQuestionsControl/getPositionQuestions.html";
    }

    @PostMapping("/admin/addPositionQuestion/end")
    public String addPositionQuestionEnd(PositionTestQuestion positionTestQuestion,PositionName positionName,LevelPosition levelPosition, Model model){
        System.out.println("add "+positionName.getIdPositionName()+" "+levelPosition.getIdLevelPosition());
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionName",positionName);
        model.addAttribute("levelPosition",levelPosition);
        model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
        if (positionTestQuestionService.getPositionTestQuestions().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("positionQuestion",positionTestQuestion);
        model.addAttribute("add","add");
        if(!(checkEmptinessPositionTestQuestion(positionTestQuestion))){
            model.addAttribute("emptyPositionQuestion","emptyPositionQuestion");
            return "admin/positionQuestionsControl/getPositionQuestions.html";
        }
        Position position=new Position();
        position.setPositionName(positionName);
        position.setLevelPosition(levelPosition);
        position=positionService.checkDuplicatePosition(position);
        positionService.addAndUpdatePosition(position);
        positionTestQuestion.setPosition(position);
        positionTestQuestionService.addAndUpdatePositionTestQuestion(positionTestQuestion);
        return "redirect:/admin/positionQuestions";
    }
    @GetMapping("/admin/editPositionQuestion/{id}")
    public String editPositionQuestion(@PathVariable("id") String idPositionQuestion, Model model){
        checkPositions();
        PositionTestQuestion positionTestQuestion=positionTestQuestionService.findPositionTestQuestionById(Integer.parseInt(idPositionQuestion));
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionName",positionTestQuestion.getPosition().getPositionName());
        model.addAttribute("levelPosition",positionTestQuestion.getPosition().getLevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
        model.addAttribute("positionTestQuestion", positionTestQuestion);
        model.addAttribute("edit","edit");
        return "admin/positionQuestionsControl/getPositionQuestions.html";
    }

    @PostMapping("/admin/editPositionQuestion/end")
    public String editPositionQuestionEnd(PositionTestQuestion positionTestQuestion,PositionName positionName,LevelPosition levelPosition, Model model){
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionName",positionName);
        model.addAttribute("levelPosition",levelPosition);
        model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
        model.addAttribute("positionTestQuestion",positionTestQuestion);
        model.addAttribute("edit","edit");
        if(!(checkEmptinessPositionTestQuestion(positionTestQuestion))){
            model.addAttribute("emptyPositionQuestion","emptyPositionQuestion");
            return "admin/positionQuestionsControl/getPositionQuestions.html";
        }
        System.out.println(positionTestQuestion.getIdPositionTestQuestion()+" id");
        Position position=new Position();
        position.setPositionName(positionName);
        position.setLevelPosition(levelPosition);
        position=positionService.checkDuplicatePosition(position);
        positionService.addAndUpdatePosition(position);
        positionTestQuestion.setPosition(position);
        positionTestQuestionService.addAndUpdatePositionTestQuestion(positionTestQuestion);
        return "redirect:/admin/positionQuestions";
    }

    @GetMapping("/admin/deletePositionQuestion/{id}")
    public String deletePositionQuestion(@PathVariable("id")String idPositionQuestion, Model model){
        checkPositions();
        PositionTestQuestion positionTestQuestion=new PositionTestQuestion();
        positionTestQuestion.setIdPositionTestQuestion(Integer.parseInt(idPositionQuestion));
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
        model.addAttribute("positionTestQuestion",positionTestQuestion);
        model.addAttribute("delete","delete");
        return "admin/positionQuestionsControl/getPositionQuestions.html";
    }

    @PostMapping("/admin/deletePositionQuestion/end")
    public String deletePositionQuestionEnd(PositionTestQuestion positionTestQuestion1, Model model){
        checkPositions();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positionNameFind", new PositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.getPositionTestQuestions());
        model.addAttribute("positionTestQuestion",positionTestQuestion1);
        model.addAttribute("delete","delete");
        PositionTestQuestion positionTestQuestion=positionTestQuestionService.findPositionTestQuestionById(positionTestQuestion1.getIdPositionTestQuestion());
        if(!(positionTestQuestionService.checkDateOfPositionTestByQuestion(positionTestQuestion))){
            model.addAttribute("notEmptyTestQuestions","notEmptyTestQuestions");
            return "admin/positionQuestionsControl/getPositionQuestions.html";
        }
        positionTestQuestionService.deleteQuestionFromTestHasQuestions(positionTestQuestion);
        return "redirect:/admin/positionQuestions";
    }

    private boolean checkEmptinessPositionTestQuestion(PositionTestQuestion positionTestQuestion){
        if(positionTestQuestion.getQuestion().isEmpty()||positionTestQuestion.getQuestion().equals("")){
            return false;
        }
        if(positionTestQuestion.getRightAnswer().isEmpty()||positionTestQuestion.getRightAnswer().equals("")){
            return false;
        }
        if(positionTestQuestion.getAnswer2().isEmpty()||positionTestQuestion.getAnswer2().equals("")){
            return false;
        }
        if(positionTestQuestion.getAnswer3().isEmpty()||positionTestQuestion.getAnswer3().equals("")){
            return false;
        }
        if(positionTestQuestion.getAnswer4().isEmpty()||positionTestQuestion.getAnswer4().equals("")){
            return false;
        }
        if(positionTestQuestion.getPoint()==0){
            return false;
        }
        return true;
    }


    private void checkPositions(){
        if(positionNameService.getPositionNames().isEmpty()) positionNameService.initializePositionName();
        if(levelPositionService.getLevelPositions().isEmpty()) levelPositionService.initializeLevelPosition();
    }
}
