package com.controller.candidate;

import com.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CandidateMainPageController {
    private final CandidateService candidateService;
    @Autowired
    public CandidateMainPageController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/candidate/candidatePage")
    public String candidateHome(Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        return "candidate/candidatePage.html";
    }

}
