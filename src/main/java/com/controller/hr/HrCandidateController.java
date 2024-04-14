package com.controller.hr;

import com.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HrCandidateController {
    ImageService imageService;
    UserDetailService userDetailService;
    CandidateService candidateService;

    public HrCandidateController(ImageService imageService, UserDetailService userDetailService, CandidateService candidateService) {
        this.imageService = imageService;
        this.userDetailService = userDetailService;
        this.candidateService = candidateService;
    }

    @GetMapping("/hr/candidates")
    public String getCandidates(Model model){
        System.out.println(candidateService.getCandidates().get(0).getUserDetail().getSNP());
        model.addAttribute("candidates", candidateService.getCandidates());
        model.addAttribute("candidateService",candidateService);
        if(candidateService.getCandidates().isEmpty()) model.addAttribute("emptiness","empty");
        return "hr/candidateControl/getCandidates.html";
    }


    @GetMapping("/hr/candidate/{id}")
    public String getCandidate(@PathVariable("id") String idCandidate, Model model){
        if(candidateService.checkActiveTrialOfCandidate(candidateService.findCandidateById(Integer.parseInt(idCandidate)))){
            model.addAttribute("candidateActivity","candidateActivity");
            model.addAttribute("vacancyId",candidateService.findCandidateById(Integer.parseInt(idCandidate)).getTrialEntities().get(0).getVacancy().getIdVacancy());
        }
        model.addAttribute("candidate", candidateService.findCandidateById(Integer.parseInt(idCandidate)));
        return "hr/candidateControl/getCandidate.html";
    }
}
