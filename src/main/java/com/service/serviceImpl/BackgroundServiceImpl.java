package com.service.serviceImpl;


import com.model.Background;
import com.model.Skill;
import com.repository.BackgroundRepository;
import com.repository.SkillRepository;
import com.service.BackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BackgroundServiceImpl implements BackgroundService {
    private final BackgroundRepository backgroundRepository;

    private final SkillRepository skillRepository;
    @Autowired
    public BackgroundServiceImpl(BackgroundRepository backgroundRepository,SkillRepository skillRepository){
        this.backgroundRepository = backgroundRepository;
        this.skillRepository=skillRepository;
    }
    @Override
    public Background addAndUpdateBackground(Background background){
        return backgroundRepository.save(background);
    }
    @Override
    public List<Background> getBackgrounds(){
        return backgroundRepository.findAll();
    }
    @Override
    public Background findBackgroundById(int id){
        return backgroundRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteBackground(int id){
        Background background=backgroundRepository.findById(id).orElse(null);
        if(background!=null){
            List<Skill> skills=background.getSkills();
            if(skills!=null){
                if(!(skills.isEmpty())){
                    for(Skill skill:skills){
                        List<Background> backgrounds=skill.getBackgrounds();
                        backgrounds.remove(background);
                        skillRepository.save(skill);
                    }
                }
            }
            backgroundRepository.deleteById(id);
        }

    }
}
