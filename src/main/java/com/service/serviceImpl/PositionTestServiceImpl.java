package com.service.serviceImpl;

import com.model.PositionTest;
import com.repository.PositionTestRepository;
import com.service.PositionTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PositionTestServiceImpl implements PositionTestService {
    private final PositionTestRepository positionTestRepository;
    @Autowired
    public PositionTestServiceImpl(PositionTestRepository positionTestRepository){
        this.positionTestRepository = positionTestRepository;
    }
    @Override
    public PositionTest addAndUpdatePositionTest(PositionTest positionTest){
        return positionTestRepository.save(positionTest);
    }
    @Override
    public List<PositionTest> getPositionTests(){
        return positionTestRepository.findAll();
    }
    @Override
    public PositionTest findPositionTestById(int id){

        return positionTestRepository.findById(id).orElse(null);
    }
    @Override
    public void deletePositionTest(int id){
        positionTestRepository.deleteById(id);
    }


}
