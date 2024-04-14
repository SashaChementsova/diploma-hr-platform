package com.controller.candidate;

import com.dto.Answer;
import com.dto.LanguageQuestionDto;
import com.dto.MainAnswer;
import com.dto.PositionQuestionDto;
import com.model.*;
import com.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CandidateTestingController {
    private final LanguageTestQuestionService languageTestQuestionService;
    private final PositionTestQuestionService positionTestQuestionService;
    private final LanguageTestHasQuestionService languageTestHasQuestionService;
    private final PositionTestHasQuestionService positionTestHasQuestionService;
    private final ResultService resultService;
    private final UserDetailService userDetailService;

    public CandidateTestingController(LanguageTestQuestionService languageTestQuestionService, PositionTestQuestionService positionTestQuestionService, LanguageTestHasQuestionService languageTestHasQuestionService, PositionTestHasQuestionService positionTestHasQuestionService, ResultService resultService, UserDetailService userDetailService) {
        this.languageTestQuestionService = languageTestQuestionService;
        this.positionTestQuestionService = positionTestQuestionService;
        this.languageTestHasQuestionService = languageTestHasQuestionService;
        this.positionTestHasQuestionService = positionTestHasQuestionService;
        this.resultService = resultService;
        this.userDetailService = userDetailService;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    @GetMapping("/candidate/startPositionTest")
    public String getPositionTest(Model model){
        PositionTest positionTest=userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().get(0).getResultTesting().getPositionTest();
        List<PositionQuestionDto> positionQuestionDtoList=new ArrayList<>();
        List<PositionTestQuestion> positionTestQuestions=positionTestQuestionService.getPositionTestQuestionByPositionTest(positionTest);
        List<Answer> answers=new ArrayList<>();
        for(PositionTestQuestion positionTestQuestion:positionTestQuestions){
            PositionQuestionDto positionQuestionDto=new PositionQuestionDto(positionTestQuestion);
            Answer answer=new Answer(positionTestQuestion.getIdPositionTestQuestion(),positionTestQuestion.getRightAnswer());
            positionQuestionDtoList.add(positionQuestionDto);
            answers.add(answer);
        }
        MainAnswer mainAnswer=new MainAnswer(answers);
        model.addAttribute("test",positionQuestionDtoList);
        model.addAttribute("answer",mainAnswer);
        List<PositionTestHasQuestion> positionTestHasQuestions=positionTest.getPositionTestHasQuestionEntities();
        for(PositionTestHasQuestion positionTestHasQuestion:positionTestHasQuestions){
            positionTestHasQuestion.setStatus("Неправильно");
            positionTestHasQuestionService.addAndUpdatePositionTestHasQuestion(positionTestHasQuestion);
        }
        Result result = positionTest.getResult();
        result.setPoints(0); resultService.addAndUpdateResult(result);
        return "candidate/positionTestControl/positionTest.html";
    }

    @PostMapping("/candidate/endPositionTest")
    public String finishPositionTest(MainAnswer mainAnswer,Model model){
        PositionTest positionTest=userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().get(0).getResultTesting().getPositionTest();
        List<PositionTestHasQuestion> positionTestHasQuestions=positionTest.getPositionTestHasQuestionEntities();
        List<Answer> answers=mainAnswer.getAnswerList();
        PositionTestHasQuestion positionTestHasQuestion;
        for(Answer answer:answers){
            if (answer.getAnswer() == null || answer.getAnswer().isEmpty()) {
                continue;
            }
            if(answer.getAnswer().equals(answer.getRightAnswer())){
                positionTestHasQuestion=positionTestHasQuestionService.findByQuestion(positionTestHasQuestions,answer.getIdQuestion());
                positionTestHasQuestion.setStatus("Правильно");
                positionTestHasQuestionService.addAndUpdatePositionTestHasQuestion(positionTestHasQuestion);
            }
        }
        Result result=positionTest.getResult();
        result.setPoints(getPointsOfPositionTest(positionTestHasQuestions,answers));
        resultService.addAndUpdateResult(result);
        return "redirect:/candidate/trial";
    }




    @GetMapping("/candidate/startLanguageTest")
    public String getLanguageTest(Model model){
        LanguageTest languageTest=userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().get(0).getResultTesting().getLanguageTestEntities().get(0);
        List<LanguageQuestionDto> languageQuestionDtoList=new ArrayList<>();
        List<LanguageTestQuestion> languageTestQuestions=languageTestQuestionService.getLanguageTestQuestionByLanguageTest(languageTest);
        List<Answer> answers=new ArrayList<>();
        for(LanguageTestQuestion languageTestQuestion:languageTestQuestions){
            LanguageQuestionDto languageQuestionDto=new LanguageQuestionDto(languageTestQuestion);
            Answer answer=new Answer(languageTestQuestion.getIdLanguageTestQuestion(),languageTestQuestion.getRightAnswer());
            languageQuestionDtoList.add(languageQuestionDto);
            answers.add(answer);
        }
        MainAnswer mainAnswer=new MainAnswer(answers);
        model.addAttribute("test",languageQuestionDtoList);
        model.addAttribute("answer",mainAnswer);
        List<LanguageTestHasQuestion> languageTestHasQuestions=languageTest.getLanguageTestHasQuestionEntities();
        for(LanguageTestHasQuestion languageTestHasQuestion:languageTestHasQuestions){
            languageTestHasQuestion.setStatus("Неправильно");
            languageTestHasQuestionService.addAndUpdateLanguageTestHasQuestion(languageTestHasQuestion);
        }
        Result result = languageTest.getResult();
        result.setPoints(0); resultService.addAndUpdateResult(result);
        return "candidate/languageTestControl/languageTest.html";
    }

    @PostMapping("/candidate/endLanguageTest")
    public String finishLanguageTest(MainAnswer mainAnswer,Model model){
        LanguageTest languageTest=userDetailService.findUserByEmail(getCurrentUsername()).getCandidate().getTrialEntities().get(0).getResultTesting().getLanguageTestEntities().get(0);
        List<LanguageTestHasQuestion> languageTestHasQuestions=languageTest.getLanguageTestHasQuestionEntities();
        List<Answer> answers=mainAnswer.getAnswerList();
        LanguageTestHasQuestion languageTestHasQuestion;
        for(Answer answer:answers){
            if (answer.getAnswer() == null || answer.getAnswer().isEmpty()) {
                continue;
            }
            if(answer.getAnswer().equals(answer.getRightAnswer())){
                languageTestHasQuestion=languageTestHasQuestionService.findByQuestion(languageTestHasQuestions,answer.getIdQuestion());
                languageTestHasQuestion.setStatus("Правильно");
                languageTestHasQuestionService.addAndUpdateLanguageTestHasQuestion(languageTestHasQuestion);
            }
        }
        Result result=languageTest.getResult();
        result.setPoints(getPointsOfLanguageTest(languageTestHasQuestions,answers));
        resultService.addAndUpdateResult(result);
        return "redirect:/candidate/trial";
    }

    private float getPointsOfLanguageTest(List<LanguageTestHasQuestion> languageTestHasQuestions,List<Answer> answers){
        float allPoints=getAllPointsFromLanguageTest(languageTestHasQuestions);
        float points=0;
        for(Answer answer:answers){
            if (answer.getAnswer() == null || answer.getAnswer().isEmpty()) {
                continue;
            }
            if(answer.getAnswer().equals(answer.getRightAnswer())){
                LanguageTestHasQuestion languageTestHasQuestion=languageTestHasQuestionService.findByQuestion(languageTestHasQuestions,answer.getIdQuestion());
                points+=languageTestHasQuestion.getLanguageTestQuestion().getPoint();
            }
        }
        points=(points*100)/allPoints;
        return (float) Math.round(points * 100) / 100;
    }

    private float getAllPointsFromLanguageTest(List<LanguageTestHasQuestion> languageTestHasQuestions){
        float allPoints=0;
        for(LanguageTestHasQuestion languageTestHasQuestion:languageTestHasQuestions){
            allPoints+=languageTestHasQuestion.getLanguageTestQuestion().getPoint();
        }
        return allPoints;
    }

    private float getPointsOfPositionTest(List<PositionTestHasQuestion> positionTestHasQuestions,List<Answer> answers){
        float allPoints=getAllPointsFromPositionTest(positionTestHasQuestions);
        float points=0;
        for(Answer answer:answers){
            if (answer.getAnswer() == null || answer.getAnswer().isEmpty()) {
                continue;
            }
            if(answer.getAnswer().equals(answer.getRightAnswer())){
                PositionTestHasQuestion positionTestHasQuestion=positionTestHasQuestionService.findByQuestion(positionTestHasQuestions,answer.getIdQuestion());
                points+=positionTestHasQuestion.getPositionTestQuestion().getPoint();
            }
        }
        points=(points*100)/allPoints;
        return  (float) Math.round(points * 100) / 100;
    }

    private float getAllPointsFromPositionTest(List<PositionTestHasQuestion> positionTestHasQuestions){
        float allPoints=0;
        for(PositionTestHasQuestion positionTestHasQuestion:positionTestHasQuestions){
            allPoints+=positionTestHasQuestion.getPositionTestQuestion().getPoint();
        }
        return allPoints;
    }

}
