package com.service;

import com.model.Skill;

import java.util.List;

public interface SkillService {
    public Skill addAndUpdateSkill(Skill skill);
    public List<Skill> getSkills();

    public Skill findSkillById(int id);
    public void deleteSkill(int id);
    public List<Skill> findSkillsByName(String name);
}
