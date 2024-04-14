package com.controller.hr;

import com.service.CandidateService;
import com.service.EmployeeService;
import com.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HrMainPageController {
    private final  EmployeeService employeeService;
    private  final CandidateService candidateService;
    @Autowired
    public HrMainPageController(EmployeeService employeeService, CandidateService candidateService) {
        this.employeeService = employeeService;
        this.candidateService = candidateService;
    }

    @GetMapping("/hr/hrPage")
    public String hrHome(Model model){
        model.addAttribute("employee",employeeService.getEmployees().size());
        model.addAttribute("candidate",candidateService.getCandidates().size());
        return "hr/hr.html";
    }

}
