package com.dto;

import com.model.LevelLanguage;

import java.util.ArrayList;
import java.util.List;

public class LevelLanguageList {
    public List<LevelLanguage> levelLanguageList;

    public LevelLanguageList() {
        levelLanguageList=new ArrayList<>();
        for(int i=0;i<5;i++){
            levelLanguageList.add(new LevelLanguage());
        }
    }
    public void addLevelLanguage(LevelLanguage levelLanguage){
        this.levelLanguageList.add(levelLanguage);
    }

    public List<LevelLanguage> getLevelLanguageList() {
        return levelLanguageList;
    }

    public void setLevelLanguageList(List<LevelLanguage> levelLanguageList) {
        this.levelLanguageList = levelLanguageList;
    }
}
