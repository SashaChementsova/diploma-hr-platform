package com.service.serviceImpl;

import com.model.Language;
import com.model.LanguageName;
import com.repository.LanguageRepository;
import com.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository){
        this.languageRepository = languageRepository;
    }
    @Override
    public Language addAndUpdateLanguage(Language language){
        return languageRepository.save(language);
    }
    @Override
    public List<Language> getLanguages(){
        return languageRepository.findAll();
    }
    @Override
    public Language findLanguageById(int id){
        return languageRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteLanguage(int id){
        languageRepository.deleteById(id);
    }
    @Override
    public void deleteLanguages(List<Language> languages){
        if(languages!=null){
            if(!(languages.isEmpty())){
                for(Language language:languages){
                    deleteLanguage(language.getIdLanguage());
                }
            }
        }
    }

    @Override
    public Language checkDuplicateLanguage(Language language){
        List<Language> languages =getLanguages();
        if(languages!=null){
            if(!(languages.isEmpty())){
                for(Language existLanguage:languages){
                    if(existLanguage.getLanguageName().getIdLanguageName()==language.getLanguageName().getIdLanguageName()&&existLanguage.getLevelLanguage().getIdLevelLanguage()==language.getLevelLanguage().getIdLevelLanguage()){
                        return existLanguage;
                    }
                }
            }
        }
        return language;
    }

    @Override
    public void deleteLanguageByLanguageName(LanguageName languageName){
        List<Language> languages=getLanguages();
        if(languages!=null){
            if (!(languages.isEmpty())){
                for(Language language:languages){
                    if(language.getLanguageName().getIdLanguageName()==languageName.getIdLanguageName()){
                        deleteLanguage(language.getIdLanguage());
                    }
                }
            }
        }
    }
}
