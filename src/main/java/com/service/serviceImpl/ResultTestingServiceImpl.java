package com.service.serviceImpl;

import com.model.ResultTesting;
import com.repository.ResultTestingRepository;
import com.service.ResultTestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResultTestingServiceImpl implements ResultTestingService {
    private final ResultTestingRepository resultTestingRepository;
    @Autowired
    public ResultTestingServiceImpl(ResultTestingRepository resultRepository){
        this.resultTestingRepository = resultRepository;
    }
    @Override
    public ResultTesting addAndUpdateResultTesting(ResultTesting resultTesting){
        return resultTestingRepository.save(resultTesting);
    }
    @Override
    public List<ResultTesting> getResultTestings(){
        return resultTestingRepository.findAll();
    }

    @Override
    public ResultTesting findResultTestingById(int id){

        return resultTestingRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteResultTesting(int id){
        resultTestingRepository.deleteById(id);
    }
}
