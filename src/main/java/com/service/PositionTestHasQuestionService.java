package com.service;

import com.model.PositionTest;
import com.model.PositionTestQuestion;
import com.model.PositionTestHasQuestion;

import java.util.List;

public interface PositionTestHasQuestionService {
    public PositionTestHasQuestion addAndUpdatePositionTestHasQuestion(PositionTestHasQuestion positionTestHasQuestion);
    public List<PositionTestHasQuestion> getPositionTestHasQuestions();
    public PositionTestHasQuestion findPositionTestHasQuestionById(int id);
    public void deletePositionTestHasQuestion(int id);
    public PositionTestHasQuestion findByQuestion(List<PositionTestHasQuestion> positionTestHasQuestions,int idQuestion);
    public  List<PositionTestHasQuestion> getPositionTestHasQuestionsByPositionTest(PositionTest positionTest);
    public void createTesting(PositionTest positionTest, List<PositionTestQuestion> positionTestQuestions);
}
