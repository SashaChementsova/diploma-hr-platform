package com.service.serviceImpl;

import com.model.*;
import com.repository.PositionTestHasQuestionRepository;
import com.repository.PositionTestQuestionRepository;
import com.service.PositionTestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PositionTestQuestionServiceImpl implements PositionTestQuestionService {
    private final PositionTestQuestionRepository positionTestQuestionRepository;
    private final PositionTestHasQuestionRepository positionTestHasQuestionRepository;
    @Autowired
    public PositionTestQuestionServiceImpl(PositionTestQuestionRepository positionTestQuestionRepository,PositionTestHasQuestionRepository positionTestHasQuestionRepository){
        this.positionTestQuestionRepository = positionTestQuestionRepository;
        this.positionTestHasQuestionRepository=positionTestHasQuestionRepository;
    }
    @Override
    public PositionTestQuestion addAndUpdatePositionTestQuestion(PositionTestQuestion positionTestQuestion){
        return positionTestQuestionRepository.save(positionTestQuestion);
    }
    @Override
    public List<PositionTestQuestion> getPositionTestQuestions(){
        return positionTestQuestionRepository.findAll();
    }
    @Override
    public PositionTestQuestion findPositionTestQuestionById(int id){

        return positionTestQuestionRepository.findById(id).orElse(null);
    }
    @Override
    public void deletePositionTestQuestion(int id){
        positionTestQuestionRepository.deleteById(id);
    }

    @Override
    public List<PositionTestQuestion> getPositionTestQuestionsByPositionName(PositionName positionName){
        List<PositionTestQuestion> positionTestQuestions=positionTestQuestionRepository.findAll();
        if(positionTestQuestions.isEmpty()) return null;
        List<PositionTestQuestion> resultPositionTestQuestions = new ArrayList<>();
        for(PositionTestQuestion positionTestQuestion:positionTestQuestions){
            if(positionTestQuestion.getPosition().getPositionName().getIdPositionName()==positionName.getIdPositionName()){
                resultPositionTestQuestions.add(positionTestQuestion);
            }
        }
        return resultPositionTestQuestions;
    }

    @Override
    public boolean checkDateOfPositionTestByQuestions(List<PositionTestQuestion> positionTestQuestions){
        for(PositionTestQuestion positionTestQuestion:positionTestQuestions){
            List<PositionTestHasQuestion> positionTestHasQuestions=positionTestQuestion.getPositionTestHasQuestionEntities();
            if(positionTestHasQuestions!=null){
                if(!(positionTestHasQuestions.isEmpty())){
                    for(PositionTestHasQuestion positionTestHasQuestion: positionTestHasQuestions){
                        PositionTest positionTest=positionTestHasQuestion.getPositionTest();
                        if(positionTest.getDate().compareTo(new java.util.Date())<=0){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkDateOfPositionTestByQuestion(PositionTestQuestion positionTestQuestion){
        List<PositionTestHasQuestion> positionTestHasQuestions=positionTestQuestion.getPositionTestHasQuestionEntities();
        if(positionTestHasQuestions!=null){
            if(!(positionTestHasQuestions.isEmpty())){
                for(PositionTestHasQuestion positionTestHasQuestion: positionTestHasQuestions){
                    PositionTest positionTest=positionTestHasQuestion.getPositionTest();
                    if(positionTest.getDate().compareTo(new java.util.Date())<=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void deleteQuestionsByPositionName(PositionName positionName){
        List<PositionTestQuestion> positionTestQuestions=getPositionTestQuestionsByPositionName(positionName);
        if(positionTestQuestions!=null){
            if(!(positionTestQuestions.isEmpty())) {
                for(PositionTestQuestion positionTestQuestion:positionTestQuestions){
                    deleteQuestionFromTestHasQuestions(positionTestQuestion);
                }
            }
        }
    }
    @Override
    public void deleteQuestionFromTestHasQuestions(PositionTestQuestion positionTestQuestion){
        List<PositionTestHasQuestion> positionTestHasQuestions=positionTestQuestion.getPositionTestHasQuestionEntities();
        if(positionTestHasQuestions!=null){
            if(!(positionTestHasQuestions.isEmpty())){
                for(PositionTestHasQuestion positionTestHasQuestion:positionTestHasQuestions){
                    positionTestHasQuestion.setPositionTestQuestion(null);
                    positionTestHasQuestionRepository.save(positionTestHasQuestion);
                }
            }
        }
        deletePositionTestQuestion(positionTestQuestion.getIdPositionTestQuestion());
    }

    @Override
    public List<PositionTestQuestion> findPositionTestQuestionsByPosition(Position position){
        List<PositionTestQuestion> positionTestQuestions=getPositionTestQuestions();
        List<PositionTestQuestion> resultPositionTestQuestions=new ArrayList<>();
        if(positionTestQuestions!=null){
            if(!(positionTestQuestions.isEmpty())){
                for(PositionTestQuestion positionTestQuestion:positionTestQuestions){
                    if(positionTestQuestion.getPosition().getIdPosition()==position.getIdPosition()){
                        resultPositionTestQuestions.add(positionTestQuestion);
                    }
                }
            }
        }
        return resultPositionTestQuestions;
    }
    @Override
    public boolean checkNumOfQuestionsByPosition(Position position,int num){
        List<PositionTestQuestion> positionTestQuestions=findPositionTestQuestionsByPosition(position);
        if(positionTestQuestions.size()>=num) return true;
        return false;
    }

    @Override
    public List<PositionTestQuestion> findQuestionsUnderPosition(Position position){
        List<PositionTestQuestion> questions=getPositionTestQuestionsByPositionName(position.getPositionName());
        List<PositionTestQuestion> positionTestQuestions=new ArrayList<>();
        if(questions!=null){
            if(!(questions.isEmpty())){
                for(PositionTestQuestion positionTestQuestion:questions){
                    if(positionTestQuestion.getPosition().getLevelPosition().getIdLevelPosition()<=position.getLevelPosition().getIdLevelPosition()){
                        positionTestQuestions.add(positionTestQuestion);
                    }
                }
            }
        }
        return positionTestQuestions;
    }

    @Override
    public List<PositionTestQuestion> generateQuestionsForTest(Position position){
        Random rand = new Random();
        List<PositionTestQuestion> positionTestQuestions=findQuestionsUnderPosition(position);
        List<PositionTestQuestion> resultQuestions=new ArrayList<>();
        int numberOfElements = 10;
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(positionTestQuestions.size());
            PositionTestQuestion randomElement = positionTestQuestions.get(randomIndex);
            resultQuestions.add(randomElement);
            positionTestQuestions.remove(randomElement);
        }
        return resultQuestions;
    }

    @Override
    public List<PositionTestQuestion> getPositionTestQuestionByPositionTest(PositionTest positionTest){
        List<PositionTestQuestion> result=new ArrayList<>();
        List<PositionTestHasQuestion> positionTestHasQuestions=positionTest.getPositionTestHasQuestionEntities();
        if(positionTestHasQuestions!=null){
            if(!(positionTestHasQuestions.isEmpty())){
                for(PositionTestHasQuestion positionTestHasQuestion:positionTestHasQuestions){
                    result.add(positionTestHasQuestion.getPositionTestQuestion());
                }
            }
        }
        return result;
    }
}
