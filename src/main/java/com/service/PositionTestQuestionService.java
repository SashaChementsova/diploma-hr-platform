package com.service;

import com.model.*;
import com.model.PositionTestQuestion;

import java.util.ArrayList;
import java.util.List;

public interface PositionTestQuestionService {
    public PositionTestQuestion addAndUpdatePositionTestQuestion(PositionTestQuestion positionTestQuestion);
    public List<PositionTestQuestion> getPositionTestQuestions();

    public PositionTestQuestion findPositionTestQuestionById(int id);
    public void deletePositionTestQuestion(int id);
    public List<PositionTestQuestion> getPositionTestQuestionsByPositionName(PositionName positionName);
    public boolean checkDateOfPositionTestByQuestions(List<PositionTestQuestion> positionTestQuestions);

    public void deleteQuestionsByPositionName(PositionName positionName);

    public boolean checkDateOfPositionTestByQuestion(PositionTestQuestion positionTestQuestion);
    public void deleteQuestionFromTestHasQuestions(PositionTestQuestion positionTestQuestion);

    
    public List<PositionTestQuestion> findPositionTestQuestionsByPosition(Position position);

    public boolean checkNumOfQuestionsByPosition(Position position,int num);
    public List<PositionTestQuestion> findQuestionsUnderPosition(Position position);

    public List<PositionTestQuestion> generateQuestionsForTest(Position position);

    public List<PositionTestQuestion> getPositionTestQuestionByPositionTest(PositionTest positionTest);
}
