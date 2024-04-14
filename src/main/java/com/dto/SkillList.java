package com.dto;

import com.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillList {
    private List<Skill> skillList;

    public SkillList() {
        skillList=new ArrayList<>();
    }
    public void addSkill(Skill skill){
        this.skillList.add(skill);
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }
}
