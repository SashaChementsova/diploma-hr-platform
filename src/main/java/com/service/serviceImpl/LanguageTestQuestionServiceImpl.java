package com.service.serviceImpl;

import com.model.*;
import com.repository.LanguageTestHasQuestionRepository;
import com.repository.LanguageTestQuestionRepository;
import com.service.LanguageTestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class LanguageTestQuestionServiceImpl implements LanguageTestQuestionService {
    private final LanguageTestQuestionRepository languageTestQuestionRepository;
    private final LanguageTestHasQuestionRepository languageTestHasQuestionRepository;
    @Autowired
    public LanguageTestQuestionServiceImpl(LanguageTestHasQuestionRepository languageTestHasQuestionRepository,LanguageTestQuestionRepository languageTestQuestionRepository){
        this.languageTestQuestionRepository = languageTestQuestionRepository;
        this.languageTestHasQuestionRepository=languageTestHasQuestionRepository;
    }
    @Override
    public LanguageTestQuestion addAndUpdateLanguageTestQuestion(LanguageTestQuestion languageTestQuestion){
        return languageTestQuestionRepository.save(languageTestQuestion);
    }
    @Override
    public List<LanguageTestQuestion> getLanguageTestQuestions(){
        return languageTestQuestionRepository.findAll();
    }
    @Override
    public LanguageTestQuestion findLanguageTestQuestionById(int id){

        return languageTestQuestionRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteLanguageTestQuestion(int id){
        languageTestQuestionRepository.deleteById(id);
    }

    @Override
    public List<LanguageTestQuestion> findLanguageTestQuestionsByLanguage(Language language){
        List<LanguageTestQuestion> languageTestQuestions=getLanguageTestQuestions();
        List<LanguageTestQuestion> resultLanguageTestQuestions=new ArrayList<>();
        if(languageTestQuestions!=null){
            if(!(languageTestQuestions.isEmpty())){
                for(LanguageTestQuestion languageTestQuestion:languageTestQuestions){
                    if(languageTestQuestion.getLanguage().getIdLanguage()==language.getIdLanguage()){
                        resultLanguageTestQuestions.add(languageTestQuestion);
                    }
                }
            }
        }
        return resultLanguageTestQuestions;
    }

    @Override
    public boolean checkDateOfLanguageTestByQuestions(List<LanguageTestQuestion> languageTestQuestions){
        for(LanguageTestQuestion languageTestQuestion:languageTestQuestions){
            List<LanguageTestHasQuestion> languageTestHasQuestions=languageTestQuestion.getLanguageTestHasQuestionEntities();
            if(languageTestHasQuestions!=null){
                if(!(languageTestHasQuestions.isEmpty())){
                    for(LanguageTestHasQuestion languageTestHasQuestion: languageTestHasQuestions){
                        LanguageTest languageTest=languageTestHasQuestion.getLanguageTest();
                        if(languageTest.getDate().compareTo(new java.util.Date())<=0){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkDateOfLanguageTestByQuestion(LanguageTestQuestion languageTestQuestion){
        if(languageTestQuestion!=null){
            List<LanguageTestHasQuestion> languageTestHasQuestions=languageTestQuestion.getLanguageTestHasQuestionEntities();
            if(languageTestHasQuestions!=null){
                if(!(languageTestHasQuestions.isEmpty())){
                    for(LanguageTestHasQuestion languageTestHasQuestion: languageTestHasQuestions){
                        LanguageTest languageTest=languageTestHasQuestion.getLanguageTest();
                        if(languageTest.getDate().compareTo(new java.util.Date())<=0){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<LanguageTestQuestion> getLanguageTestQuestionsByLanguageName(LanguageName languageName){
        List<LanguageTestQuestion> languageTestQuestions=languageTestQuestionRepository.findAll();
        if(languageTestQuestions.isEmpty()) return null;
        List<LanguageTestQuestion> resultLanguageTestQuestions = new ArrayList<>();
        for(LanguageTestQuestion languageTestQuestion:languageTestQuestions){
            if(languageTestQuestion.getLanguage().getLanguageName().getIdLanguageName()==languageName.getIdLanguageName()){
                resultLanguageTestQuestions.add(languageTestQuestion);
            }
        }
        return resultLanguageTestQuestions;
    }
    @Override
    public void deleteQuestionsByLanguageName(LanguageName languageName){
        List<LanguageTestQuestion> languageTestQuestions=getLanguageTestQuestionsByLanguageName(languageName);
        if(languageTestQuestions!=null){
            if(!(languageTestQuestions.isEmpty())) {
                for(LanguageTestQuestion languageTestQuestion:languageTestQuestions){
                    deleteQuestionFromTestHasQuestions(languageTestQuestion);
                }
            }
        }
    }

    @Override
    public void deleteQuestionFromTestHasQuestions(LanguageTestQuestion languageTestQuestion){
        List<LanguageTestHasQuestion> languageTestHasQuestions=languageTestQuestion.getLanguageTestHasQuestionEntities();
        if(languageTestHasQuestions!=null){
            if(!(languageTestHasQuestions.isEmpty())){
                for(LanguageTestHasQuestion languageTestHasQuestion:languageTestHasQuestions){
                    languageTestHasQuestion.setLanguageTestQuestion(null);
                    languageTestHasQuestionRepository.save(languageTestHasQuestion);
                }
            }
        }
        deleteLanguageTestQuestion(languageTestQuestion.getIdLanguageTestQuestion());
    }

    @Override
    public boolean checkNumOfQuestionsByLanguage(Language language, int num){
        List<LanguageTestQuestion> languageTestQuestions=findLanguageTestQuestionsByLanguage(language);
        if(languageTestQuestions.size()>=num) return true;
        return false;
    }

    public List<LanguageTestQuestion> findQuestionsUnderLanguage(Language language){
        List<LanguageTestQuestion> questions=getLanguageTestQuestionsByLanguageName(language.getLanguageName());
        List<LanguageTestQuestion> languageTestQuestions=new ArrayList<>();
        if(questions!=null){
            if(!(questions.isEmpty())){
                for(LanguageTestQuestion languageTestQuestion:questions){
                    if(languageTestQuestion.getLanguage().getLevelLanguage().getIdLevelLanguage()<=language.getLevelLanguage().getIdLevelLanguage()){
                        languageTestQuestions.add(languageTestQuestion);
                    }
                }
            }
        }
        return languageTestQuestions;
    }
    @Override
    public List<LanguageTestQuestion> generateQuestionsForTest(Language language){
        Random rand = new Random();
        List<LanguageTestQuestion> languageTestQuestions=findQuestionsUnderLanguage(language);
        List<LanguageTestQuestion> resultQuestions=new ArrayList<>();
        int numberOfElements = 10;
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(languageTestQuestions.size());
            LanguageTestQuestion randomElement = languageTestQuestions.get(randomIndex);
            resultQuestions.add(randomElement);
            languageTestQuestions.remove(randomElement);
        }
        return resultQuestions;
    }
    @Override
    public List<LanguageTestQuestion> getLanguageTestQuestionByLanguageTest(LanguageTest languageTest){
        List<LanguageTestQuestion> result=new ArrayList<>();
        List<LanguageTestHasQuestion> languageTestHasQuestions=languageTest.getLanguageTestHasQuestionEntities();
        if(languageTestHasQuestions!=null){
            if(!(languageTestHasQuestions.isEmpty())){
                for(LanguageTestHasQuestion languageTestHasQuestion:languageTestHasQuestions){
                    result.add(languageTestHasQuestion.getLanguageTestQuestion());
                }
            }
        }
        return result;
    }
}
