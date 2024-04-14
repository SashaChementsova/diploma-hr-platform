package com.controller.admin;

import com.service.CandidateService;
import com.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMainPageController {
    private final CandidateService candidateService;
    private final EmployeeService employeeService;
    @Autowired
    public AdminMainPageController(CandidateService candidateService, EmployeeService employeeService) {
        this.candidateService = candidateService;
        this.employeeService = employeeService;
    }

    @GetMapping("/admin/adminPage")
    public String adminHome(Model model){
        model.addAttribute("employee",employeeService.getEmployees().size());
        model.addAttribute("candidate",candidateService.getCandidates().size());
        return "admin/adminPage.html";
    }
}
