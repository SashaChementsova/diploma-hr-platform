package com.controller.employee;

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

@Controller
public class EmployeePositionQuestionsController {
    private final PositionNameService positionNameService;
    private final PositionService positionService;
    private final LevelPositionService levelPositionService;
    private final PositionTestQuestionService positionTestQuestionService;

    private final UserDetailService userDetailService;
    @Autowired
    public EmployeePositionQuestionsController(UserDetailService userDetailService,EmployeeService employeeService,PositionNameService positionNameService, PositionService positionService, LevelPositionService levelPositionService, PositionTestQuestionService positionTestQuestionService) {
        this.positionNameService = positionNameService;
        this.userDetailService=userDetailService;
        this.positionService = positionService;
        this.levelPositionService = levelPositionService;
        this.positionTestQuestionService = positionTestQuestionService;
    }
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/employee/positionQuestions")
    public String getPositionQuestions(LevelPosition levelPosition, Model model) {
        checkPositions();
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        model.addAttribute("levelPositions", levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        if (levelPosition.getIdLevelPosition()!=0) {
            model.addAttribute("levelPositionFind", levelPosition);
            position.setLevelPosition(levelPosition);
            position=positionService.addAndUpdatePosition(positionService.checkDuplicatePosition(position));
            model.addAttribute("positionQuestions", positionTestQuestionService.findPositionTestQuestionsByPosition(position));
            if (positionTestQuestionService.findPositionTestQuestionsByPosition(position).isEmpty())
                model.addAttribute("emptiness", "empty");
        } else {
            position.setLevelPosition(employee.getPosition().getLevelPosition());
            position=positionService.addAndUpdatePosition(positionService.checkDuplicatePosition(position));
            model.addAttribute("levelPositionFind", new LevelPosition());
            model.addAttribute("positionQuestions", positionTestQuestionService.findQuestionsUnderPosition(position));
            if (positionTestQuestionService.findQuestionsUnderPosition(position).isEmpty())
                model.addAttribute("emptiness", "empty");
        }
        return "employee/positionQuestionsControl/getPositionQuestions.html";
    }

    @GetMapping("/employee/addPositionQuestion")
    public String addPositionQuestion(Model model){
        checkPositions();
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        model.addAttribute("levelPositions", levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        position.setLevelPosition(employee.getPosition().getLevelPosition());
        model.addAttribute("levelPositionFind",new LevelPosition());
        model.addAttribute("levelPosition",new LevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.findQuestionsUnderPosition(position));
        if (positionTestQuestionService.findQuestionsUnderPosition(position).isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("positionTestQuestion", new PositionTestQuestion());
        model.addAttribute("add","add");
        return "employee/positionQuestionsControl/getPositionQuestions.html";
    }

    @PostMapping("/employee/addPositionQuestion/end")
    public String addPositionQuestionEnd(PositionTestQuestion positionTestQuestion,LevelPosition levelPosition, Model model){
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        model.addAttribute("levelPositions",levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        position.setLevelPosition(employee.getPosition().getLevelPosition());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("levelPosition",levelPosition);
        model.addAttribute("positionQuestions", positionTestQuestionService.findQuestionsUnderPosition(position));
        if (positionTestQuestionService.getPositionTestQuestions().isEmpty())
            model.addAttribute("emptiness", "empty");
        model.addAttribute("positionQuestion",positionTestQuestion);
        model.addAttribute("add","add");
        if(!(checkEmptinessPositionTestQuestion(positionTestQuestion))){
            model.addAttribute("emptyPositionQuestion","emptyPositionQuestion");
            return "employee/positionQuestionsControl/getPositionQuestions.html";
        }
        position.setLevelPosition(levelPosition);
        position=positionService.checkDuplicatePosition(position);
        positionService.addAndUpdatePosition(position);
        positionTestQuestion.setPosition(position);
        positionTestQuestionService.addAndUpdatePositionTestQuestion(positionTestQuestion);
        return "redirect:/employee/positionQuestions";
    }
    @GetMapping("/employee/editPositionQuestion/{id}")
    public String editPositionQuestion(@PathVariable("id") String idPositionQuestion, Model model){
        checkPositions();
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        PositionTestQuestion positionTestQuestion=positionTestQuestionService.findPositionTestQuestionById(Integer.parseInt(idPositionQuestion));
        model.addAttribute("levelPositions",levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        position.setLevelPosition(employee.getPosition().getLevelPosition());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("levelPosition",positionTestQuestion.getPosition().getLevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.findQuestionsUnderPosition(position));
        model.addAttribute("positionTestQuestion", positionTestQuestion);
        model.addAttribute("edit","edit");
        return "employee/positionQuestionsControl/getPositionQuestions.html";
    }

    @PostMapping("/employee/editPositionQuestion/end")
    public String editPositionQuestionEnd(PositionTestQuestion positionTestQuestion,LevelPosition levelPosition, Model model){
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        model.addAttribute("levelPositions",levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("levelPosition",levelPosition);
        model.addAttribute("positionQuestions",positionTestQuestionService.findQuestionsUnderPosition(position));
        model.addAttribute("positionTestQuestion",positionTestQuestion);
        model.addAttribute("edit","edit");
        if(!(checkEmptinessPositionTestQuestion(positionTestQuestion))){
            model.addAttribute("emptyPositionQuestion","emptyPositionQuestion");
            return "employee/positionQuestionsControl/getPositionQuestions.html";
        }
        position.setLevelPosition(levelPositionService.findLevelPositionById(levelPosition.getIdLevelPosition()));
        position=positionService.checkDuplicatePosition(position);
        positionService.addAndUpdatePosition(position);
        positionTestQuestion.setPosition(position);
        positionTestQuestionService.addAndUpdatePositionTestQuestion(positionTestQuestion);
        return "redirect:/employee/positionQuestions";
    }

    @GetMapping("/employee/deletePositionQuestion/{id}")
    public String deletePositionQuestion(@PathVariable("id")String idPositionQuestion, Model model){
        checkPositions();
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        PositionTestQuestion positionTestQuestion=new PositionTestQuestion();
        positionTestQuestion.setIdPositionTestQuestion(Integer.parseInt(idPositionQuestion));
        model.addAttribute("levelPositions",levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        position.setLevelPosition(employee.getPosition().getLevelPosition());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.findQuestionsUnderPosition(position));
        model.addAttribute("positionTestQuestion",positionTestQuestion);
        model.addAttribute("delete","delete");
        return "employee/positionQuestionsControl/getPositionQuestions.html";
    }

    @PostMapping("/employee/deletePositionQuestion/end")
    public String deletePositionQuestionEnd(PositionTestQuestion positionTestQuestion1, Model model){
        checkPositions();
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        Employee employee=userDetail.getEmployee();
        model.addAttribute("levelPositions", levelPositionService.getLevelPositionsUnderPosition(employee.getPosition()));
        Position position=new Position();
        position.setPositionName(employee.getPosition().getPositionName());
        position.setLevelPosition(employee.getPosition().getLevelPosition());
        model.addAttribute("levelPositionFind", new LevelPosition());
        model.addAttribute("positionQuestions", positionTestQuestionService.findQuestionsUnderPosition(position));
        model.addAttribute("positionTestQuestion",positionTestQuestion1);
        model.addAttribute("delete","delete");
        PositionTestQuestion positionTestQuestion=positionTestQuestionService.findPositionTestQuestionById(positionTestQuestion1.getIdPositionTestQuestion());
        if(!(positionTestQuestionService.checkDateOfPositionTestByQuestion(positionTestQuestion))){
            model.addAttribute("notEmptyTestQuestions","notEmptyTestQuestions");
            return "employee/positionQuestionsControl/getPositionQuestions.html";
        }
        positionTestQuestionService.deleteQuestionFromTestHasQuestions(positionTestQuestion);
        return "redirect:/employee/positionQuestions";
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
