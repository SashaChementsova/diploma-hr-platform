package com.service.serviceImpl;

import com.model.Position;
import com.model.Position;
import com.model.PositionName;
import com.repository.PositionRepository;
import com.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository){
        this.positionRepository = positionRepository;
    }
    @Override
    public Position addAndUpdatePosition(Position position){
        return positionRepository.save(position);
    }
    @Override
    public List<Position> getPositions(){
        return positionRepository.findAll();
    }
    @Override
    public Position findPositionById(int id){

        return positionRepository.findById(id).orElse(null);
    }
    @Override
    public void deletePosition(int id){
        positionRepository.deleteById(id);
    }
    @Override
    public void deletePositionByPositionName(PositionName positionName){
        List<Position> positions=getPositions();
        if(positions!=null){
            if (!(positions.isEmpty())){
                for(Position position:positions){
                    if(position.getPositionName().getIdPositionName()==positionName.getIdPositionName()){
                        deletePosition(position.getIdPosition());
                    }
                }
            }
        }
    }

    public Position checkDuplicatePosition(Position position){
        List<Position> positions =getPositions();
        if(positions!=null){
            if(!(positions.isEmpty())){
                for(Position existPosition:positions){
                    if(existPosition.getPositionName().getIdPositionName()==position.getPositionName().getIdPositionName()&&existPosition.getLevelPosition().getIdLevelPosition()==position.getLevelPosition().getIdLevelPosition()){
                        return existPosition;
                    }
                }
            }
        }
        return position;
    }

}
