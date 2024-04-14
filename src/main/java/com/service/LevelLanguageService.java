package com.service;

import com.model.LevelLanguage;

import java.util.List;

public interface LevelLanguageService {
    public LevelLanguage addAndUpdateLevelLanguage(LevelLanguage levelLanguage);

    public List<LevelLanguage> getLevelLanguages();

    public LevelLanguage findLevelLanguageById(int id);

    public void deleteLevelLanguage(int id);

    public void initializeLevelLanguage();
}
