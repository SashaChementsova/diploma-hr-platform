package com.service;

import com.model.*;

import java.util.List;

public interface LanguageTestQuestionService {
    public LanguageTestQuestion addAndUpdateLanguageTestQuestion(LanguageTestQuestion languageTestQuestion);
    public List<LanguageTestQuestion> getLanguageTestQuestions();
    public LanguageTestQuestion findLanguageTestQuestionById(int id);
    public void deleteLanguageTestQuestion(int id);

    public List<LanguageTestQuestion> findLanguageTestQuestionsByLanguage(Language language);

    public boolean checkDateOfLanguageTestByQuestions(List<LanguageTestQuestion> languageTestQuestions);
    public void deleteQuestionsByLanguageName(LanguageName languageName);
    public List<LanguageTestQuestion> getLanguageTestQuestionsByLanguageName(LanguageName languageName);
    public boolean checkDateOfLanguageTestByQuestion(LanguageTestQuestion languageTestQuestion);
    public void deleteQuestionFromTestHasQuestions(LanguageTestQuestion languageTestQuestion);

    public boolean checkNumOfQuestionsByLanguage(Language language, int num);

    public List<LanguageTestQuestion> findQuestionsUnderLanguage(Language language);

    public List<LanguageTestQuestion> generateQuestionsForTest(Language language);

    public List<LanguageTestQuestion> getLanguageTestQuestionByLanguageTest(LanguageTest languageTest);
}
