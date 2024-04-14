package com.service.serviceImpl;

import com.model.LevelPosition;
import com.model.Position;
import com.model.PositionName;
import com.repository.LevelPositionRepository;
import com.service.LevelPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class LevelPositionServiceImpl implements LevelPositionService {
    private final LevelPositionRepository levelPositionRepository;
    @Autowired
    public LevelPositionServiceImpl(LevelPositionRepository levelPositionRepository){
        this.levelPositionRepository = levelPositionRepository;
    }
    @Override
    public LevelPosition addAndUpdateLevelPosition(LevelPosition levelPosition){
        return levelPositionRepository.save(levelPosition);
    }
    @Override
    public List<LevelPosition> getLevelPositions(){
        return levelPositionRepository.findAll();
    }
    @Override
    public LevelPosition findLevelPositionById(int id){

        return levelPositionRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteLevelPosition(int id){
        levelPositionRepository.deleteById(id);
    }

    @Override
    public void initializeLevelPosition(){
        try{
            levelPositionRepository.save(new LevelPosition("Junior"));
            levelPositionRepository.save(new LevelPosition("Middle"));
            levelPositionRepository.save(new LevelPosition("Senior"));
        }
        catch (Exception ex){
            System.out.println("Значения уже есть");
        }

    }

    public List<LevelPosition> getLevelPositionsUnderPosition(Position position){
        List<LevelPosition> levelPositions=getLevelPositions();
        List<LevelPosition> resultLevelPositions=new ArrayList<>();
        if(levelPositions!=null){
            if(!(levelPositions.isEmpty())){
                for(LevelPosition levelPosition:levelPositions){
                    if(levelPosition.getIdLevelPosition()<=position.getLevelPosition().getIdLevelPosition()){
                        resultLevelPositions.add(levelPosition);
                    }
                }
            }
        }
        return resultLevelPositions;
    }
}
