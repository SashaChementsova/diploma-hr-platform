package com.controller.employee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeMainPageController {
    @GetMapping("/employee/employeePage")
    public String employeeHome(Model model){
        return "employee/employee.html";
    }
}
