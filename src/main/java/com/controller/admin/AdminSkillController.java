package com.controller.admin;

import com.dto.DataString;
import com.model.Skill;
import com.service.BackgroundService;
import com.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminSkillController {
    private final SkillService skillService;
    private final BackgroundService backgroundService;
    @Autowired
    public AdminSkillController(SkillService skillService, BackgroundService backgroundService) {
        this.skillService = skillService;
        this.backgroundService = backgroundService;
    }

    @GetMapping("/admin/skills")
    public String getSkills(DataString skillName, Model model){
        if(skillName.getData()==null){
            model.addAttribute("skills",skillService.getSkills());
            model.addAttribute("nameSkill",new DataString());
            if(skillService.getSkills().isEmpty()) model.addAttribute("emptiness","empty");
        }
        else{
            skillName.setData(skillName.getData().trim());
            List<Skill> skills=skillService.findSkillsByName(skillName.getData());
            model.addAttribute("skills",skills);
            model.addAttribute("nameSkill",skillName);
            if(skills.isEmpty()) model.addAttribute("emptiness","empty");
        }
        return "admin/skillControl/getSkills.html";
    }
    

    @GetMapping("/admin/addSkill")
    public String addSkill(Model model){
        model.addAttribute("skill", new Skill());
        model.addAttribute("add","add");
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("nameSkill",new DataString());
        if(skillService.getSkills().isEmpty()) model.addAttribute("emptiness","empty");
        return "admin/skillControl/getSkills.html";
    }

    @PostMapping("/admin/addSkill/end")
    public String addSkillEnd(Skill skill, Model model){
        model.addAttribute("skill", skill);
        model.addAttribute("add","add");
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("nameSkill",new DataString());
        if(skillService.getSkills().isEmpty()) model.addAttribute("emptiness","empty");
        if(skill.getNameSkill().isEmpty()||skill.getNameSkill().equals("")){
            model.addAttribute("emptySkill","emptySkill");
            return "admin/skillControl/getSkills.html";
        }
        try{
            skillService.addAndUpdateSkill(skill);
        }
        catch (Exception ex){
            model.addAttribute("fail","fail");
            return "admin/skillControl/getSkills.html";
        }
        return "redirect:/admin/skills";
    }

    @GetMapping("/admin/editSkill/{id}")
    public String editSkill(@PathVariable("id") String idSkill, Model model){
        model.addAttribute("edit","edit");
        model.addAttribute("skill",skillService.findSkillById(Integer.parseInt(idSkill)));
        model.addAttribute("skills", skillService.getSkills());
        model.addAttribute("nameSkill",new DataString());
        return "admin/skillControl/getSkills.html";
    }

    @PostMapping("/admin/editSkill/end")
    public String editSkillEnd(Skill skill, Model model){
        model.addAttribute("skill", skill);
        model.addAttribute("edit","edit");
        model.addAttribute("skills",skillService.getSkills());
        model.addAttribute("nameSkill",new DataString());
        if(skill.getNameSkill().isEmpty()||skill.getNameSkill().equals("")){
            model.addAttribute("empty","empty");
            return "admin/skillControl/getSkills.html";
        }
        try {
            skillService.addAndUpdateSkill(skill);
        }
        catch (Exception ex){
            model.addAttribute("fail","fail");
            return "admin/skillControl/getSkills.html";
        }
        return "redirect:/admin/skills";
    }

    @GetMapping("/admin/deleteSkill/{id}")
    public String deleteSkill(@PathVariable("id")String idSkill, Model model){
        Skill skill= new Skill();
        skill.setIdSkill(Integer.parseInt(idSkill));
        model.addAttribute("delete","delete");
        model.addAttribute("skill", skill);
        model.addAttribute("skills", skillService.getSkills());
        model.addAttribute("nameSkill",new DataString());
        return "admin/skillControl/getSkills.html";
    }

    @PostMapping("/admin/deleteSkill/end")
    public String deleteSkillEnd(Skill skill, Model model){
        model.addAttribute("delete","delete");
        model.addAttribute("skill", skill);
        model.addAttribute("skills", skillService.getSkills());
        model.addAttribute("nameSkill",new DataString());
        Skill skill1=skillService.findSkillById(skill.getIdSkill());
        if(skill1.getBackgrounds()!=null){
            if(!(skill1.getBackgrounds().isEmpty())){
                model.addAttribute("notEmptyBackground","notEmptyBackground");
                return "admin/skillControl/getSkills.html";
            }
        }
        skillService.deleteSkill(skill1.getIdSkill());
        return "redirect:/admin/skills";
    }
    
}
