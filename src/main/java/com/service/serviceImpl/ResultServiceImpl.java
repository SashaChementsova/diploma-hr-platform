package com.service.serviceImpl;

import com.model.Result;
import com.repository.ResultRepository;
import com.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository){
        this.resultRepository = resultRepository;
    }
    @Override
    public Result addAndUpdateResult(Result result){
        return resultRepository.save(result);
    }
    @Override
    public List<Result> getResults(){
        return resultRepository.findAll();
    }

    @Override
    public Result findResultById(int id){

        return resultRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteResult(int id){
        resultRepository.deleteById(id);
    }
}
