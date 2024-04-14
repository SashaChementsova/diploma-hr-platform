package com.service.serviceImpl;

import com.model.LanguageTest;
import com.repository.LanguageTestRepository;
import com.service.LanguageTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LanguageTestServiceImpl implements LanguageTestService {
    private final LanguageTestRepository languageTestRepository;
    @Autowired
    public LanguageTestServiceImpl(LanguageTestRepository languageTestRepository){
        this.languageTestRepository = languageTestRepository;
    }
    @Override
    public LanguageTest addAndUpdateLanguageTest(LanguageTest languageTest){
        return languageTestRepository.save(languageTest);
    }
    @Override
    public List<LanguageTest> getLanguageTests(){
        return languageTestRepository.findAll();
    }
    @Override
    public LanguageTest findLanguageTestById(int id){

        return languageTestRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteLanguageTest(int id){
        languageTestRepository.deleteById(id);
    }
}
