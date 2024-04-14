package com.service;

import com.model.Language;
import com.model.LanguageName;

import java.util.List;

public interface LanguageService {
    public Language addAndUpdateLanguage(Language language);

    public List<Language> getLanguages();
    public Language findLanguageById(int id);

    public void deleteLanguage(int id);

    public void deleteLanguages(List<Language> languages);

    public Language checkDuplicateLanguage(Language language);
    public void deleteLanguageByLanguageName(LanguageName languageName);
}
