package com.service;

import com.model.LanguageTest;
import com.model.LanguageTestHasQuestion;
import com.model.LanguageTestQuestion;

import java.util.List;

public interface LanguageTestHasQuestionService {
    public LanguageTestHasQuestion addAndUpdateLanguageTestHasQuestion(LanguageTestHasQuestion languageTestHasQuestion);
    public List<LanguageTestHasQuestion> getLanguageTestHasQuestions();

    public LanguageTestHasQuestion findLanguageTestHasQuestionById(int id);
    public void deleteLanguageTestHasQuestion(int id);
    public  List<LanguageTestHasQuestion> getLanguageTestHasQuestionsByLanguageTest(LanguageTest languageTest);
    public LanguageTestHasQuestion findByQuestion(List<LanguageTestHasQuestion> languageTestHasQuestions,int idQuestion);
    public void createTesting(LanguageTest languageTest, List<LanguageTestQuestion> languageTestQuestions);
}
