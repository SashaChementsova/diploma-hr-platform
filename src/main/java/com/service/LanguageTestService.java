package com.service;

import com.model.LanguageTest;

import java.util.List;

public interface LanguageTestService {
    public LanguageTest addAndUpdateLanguageTest(LanguageTest languageTest);
    public List<LanguageTest> getLanguageTests();

    public LanguageTest findLanguageTestById(int id);
    public void deleteLanguageTest(int id);
}
