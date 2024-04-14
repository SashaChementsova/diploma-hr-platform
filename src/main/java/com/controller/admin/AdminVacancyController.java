package com.controller.admin;

import com.model.LevelPosition;
import com.model.Position;
import com.model.PositionName;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminVacancyController {
    private final VacancyService vacancyService;
    private final PositionNameService positionNameService;
    private final LevelPositionService levelPositionService;
    private final PositionService positionService;
    private final HrService hrService;
    private final CandidateService candidateService;
    @Autowired
    public AdminVacancyController(VacancyService vacancyService, PositionNameService positionNameService, LevelPositionService levelPositionService, PositionService positionService, HrService hrService, CandidateService candidateService) {
        this.vacancyService = vacancyService;
        this.positionNameService = positionNameService;
        this.levelPositionService = levelPositionService;
        this.positionService = positionService;
        this.hrService = hrService;
        this.candidateService = candidateService;
    }

    @GetMapping("/admin/vacancies")
    public String getVacancies(PositionName positionName, LevelPosition levelPosition, Model model) {
        checkPositions();
        model.addAttribute("positionNames", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        if (positionName.getIdPositionName()!=0 && levelPosition.getIdLevelPosition()!=0) {
            model.addAttribute("positionNameFind", positionName);
            model.addAttribute("levelPositionFind", levelPosition);
            Position position=new Position();
            position.setLevelPosition(levelPosition); position.setPositionName(positionName);
            position=positionService.addAndUpdatePosition(positionService.checkDuplicatePosition(position));
            model.addAttribute("vacancies", vacancyService.getActiveVacanciesByPosition(position));
            if ( vacancyService.findVacanciesByPosition(position).isEmpty())
                model.addAttribute("emptiness", "empty");
        } else {
            model.addAttribute("positionNameFind", new PositionName());
            model.addAttribute("levelPositionFind", new LevelPosition());
            model.addAttribute("vacancies", vacancyService.getActiveVacancies());
            if (vacancyService.getActiveVacancies().isEmpty())
                model.addAttribute("emptiness", "empty");
        }
        return "admin/vacancyControl/getVacancies.html";
    }

    @GetMapping("/admin/vacancy/{id}")
    public String getVacancy(@PathVariable("id") String idVacancy, Model model) {
        checkPositions();
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        return "admin/vacancyControl/getVacancy.html";
    }

    @GetMapping("/admin/vacancyHr/{idVacancy}/{idHr}")
    public String getVacancyHr(@PathVariable("idVacancy") String idVacancy,@PathVariable("idHr") String idHr, Model model) {
        checkPositions();
        model.addAttribute("hr",hrService.findHrById(Integer.parseInt(idHr)));
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        return "admin/vacancyControl/getVacancyHr.html";
    }

    @GetMapping("/admin/vacancyHr/{idVacancy}/{idCandidate}")
    public String getVacancyCandidate(@PathVariable("idVacancy") String idVacancy,@PathVariable("idCandidate") String idCandidate, Model model) {
        checkPositions();
        model.addAttribute("candidate",candidateService.findCandidateById(Integer.parseInt(idCandidate)));
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.parseInt(idVacancy)));
        return "admin/vacancyControl/getVacancyCandidate.html";
    }

    private void checkPositions(){
        if(positionNameService.getPositionNames().isEmpty()) positionNameService.initializePositionName();
        if(levelPositionService.getLevelPositions().isEmpty()) levelPositionService.initializeLevelPosition();
    }
}
