package com.service;

import com.model.LanguageName;

import java.util.List;

public interface LanguageNameService {
    public LanguageName addAndUpdateLanguageName(LanguageName languageName);
    public List<LanguageName> getLanguageNames();

    public LanguageName findLanguageNameById(int id);
    public void deleteLanguageName(int id);

    public void initializeLanguageName();

    public boolean checkLanguageNameByEmployees(LanguageName languageName);
    public boolean checkLanguageNameByVacancies(LanguageName languageName);
    public boolean checkLanguageNameByCandidate(LanguageName languageName);
    public boolean checkLanguageNameByTestQuestions(LanguageName languageName);
}
