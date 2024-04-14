package com.controller.visitor;

import com.model.LevelPosition;
import com.model.Position;
import com.model.PositionName;
import com.service.LevelPositionService;
import com.service.PositionNameService;
import com.service.PositionService;
import com.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainPageController {

    private final VacancyService vacancyService;
    private final PositionNameService positionNameService;
    private final LevelPositionService levelPositionService;

    private final PositionService positionService;
    @Autowired
    public MainPageController(PositionService positionService,VacancyService vacancyService, PositionNameService positionNameService, LevelPositionService levelPositionService) {
        this.vacancyService = vacancyService;
        this.positionNameService = positionNameService;
        this.levelPositionService = levelPositionService;
        this.positionService=positionService;
    }




    @GetMapping("/")
    public String home() {
        return "mainPage.html";
    }

    @GetMapping("/visitor/vacancies")
    public String getVacancies(PositionName positionName, LevelPosition levelPosition, Model model){
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
        return "getVacancies.html";
    }

    @GetMapping("/visitor/vacancy/{id}")
    public String getVacancy(@PathVariable("id") String idVacancy, Model model){
        model.addAttribute("vacancy",vacancyService.findVacancyById(Integer.getInteger(idVacancy)));
        return "getVacancies.html";
    }

}
