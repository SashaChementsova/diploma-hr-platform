package com.dto;

import com.model.LanguageName;

import java.util.ArrayList;
import java.util.List;

public class LanguageNameList {
    public List<LanguageName> languageNameList;
    public LanguageNameList(){
        languageNameList=new ArrayList<>();
        for(int i=0;i<3;i++){
            languageNameList.add(new LanguageName());
        }
    }
    public void addLanguageName(LanguageName languageName){
        this.languageNameList.add(languageName);
    }

    public List<LanguageName> getLanguageNameList() {
        return languageNameList;
    }

    public void setLanguageNameList(List<LanguageName> languageNameList) {
        this.languageNameList = languageNameList;
    }
}
