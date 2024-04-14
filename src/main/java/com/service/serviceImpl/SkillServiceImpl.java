package com.service.serviceImpl;

import com.model.Skill;
import com.repository.SkillRepository;
import com.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    }
    @Override
    public Skill addAndUpdateSkill(Skill skill){
        return skillRepository.save(skill);
    }
    @Override
    public List<Skill> getSkills(){
        return skillRepository.findAll();
    }
    @Override
    public Skill findSkillById(int id){

        return skillRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteSkill(int id){
        skillRepository.deleteById(id);
    }

    @Override
    public List<Skill> findSkillsByName(String name){
        List<Skill> skills=getSkills();
        List<Skill> resultSkills=new ArrayList<>();
        if(skills!=null){
            if(!(skills.isEmpty())){
                for(Skill skill:skills){
                    if(skill.getNameSkill().contains(name)){
                        resultSkills.add(skill);
                    }
                }
            }
        }
        return resultSkills;
    }
}
