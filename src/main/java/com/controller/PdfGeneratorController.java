package com.controller;

import com.model.Employee;
import com.model.UserDetail;
import com.service.AdminService;
import com.service.EmployeeService;
import com.service.HrService;
import com.service.serviceImpl.DataMapperServiceImpl;
import com.service.serviceImpl.DocumentGeneratorServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Controller
public class PdfGeneratorController {


    @Autowired
    private DocumentGeneratorServiceImpl documentGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private DataMapperServiceImpl dataMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HrService hrService;

    @Autowired
    private AdminService adminService;

    @GetMapping(value = "/contract/{idPosition}/{idEmployee}")
    public String generateDocument(@PathVariable("idPosition")String idPosition, @PathVariable("idEmployee")String idEmployee) {
        checkAdmin();
        String finalHtml = null;

        Context dataContext = dataMapper.setData(employeeService.findEmployeeById(Integer.parseInt(idEmployee)),adminService.findAdminById(1));

        finalHtml = springTemplateEngine.process("Contract", dataContext);


        documentGenerator.htmlToPdf(finalHtml);

        return "redirect:/admin/employeeOfPosition/"+idEmployee;
    }

    @GetMapping(value = "/contract/{idEmployee}")
    public String generateDocumentEmployee(@PathVariable("idEmployee")String idEmployee) {
        checkAdmin();
        String finalHtml = null;

        Context dataContext = dataMapper.setData(employeeService.findEmployeeById(Integer.parseInt(idEmployee)),adminService.findAdminById(1));

        finalHtml = springTemplateEngine.process("Contract", dataContext);


        documentGenerator.htmlToPdf(finalHtml);

        return "redirect:/admin/employee/"+idEmployee;
    }


    @GetMapping(value = "/hr/contractHr/{idEmployee}")
    public String generateDocumentHrEmployee(@PathVariable("idEmployee")String idEmployee) {
        checkAdmin();
        String finalHtml = null;

        Context dataContext = dataMapper.setData(employeeService.findEmployeeById(Integer.parseInt(idEmployee)),adminService.findAdminById(1));

        finalHtml = springTemplateEngine.process("Contract", dataContext);


        documentGenerator.htmlToPdf(finalHtml);

        return "redirect:/hr/getProfile";
    }

    @GetMapping(value = "/employee/contractEmployee/{idEmployee}")
    public String generateDocumentEmployeeProfile(@PathVariable("idEmployee")String idEmployee) {
        checkAdmin();
        String finalHtml = null;

        Context dataContext = dataMapper.setData(employeeService.findEmployeeById(Integer.parseInt(idEmployee)),adminService.findAdminById(1));

        finalHtml = springTemplateEngine.process("Contract", dataContext);


        documentGenerator.htmlToPdf(finalHtml);

        return "redirect:/employee/getProfile";
    }

    @GetMapping(value = "/contractHrVacancy/{idVacancy}/{idHr}")
    public String generateDocumentHrVacancy(@PathVariable("idVacancy")String idVacancy,@PathVariable("idHr")String idHr) {
        checkAdmin();
        String finalHtml = null;

        Employee employee= hrService.findHrById(Integer.parseInt(idHr)).getUserDetail().getEmployee();
        Context dataContext = dataMapper.setData(employee,adminService.findAdminById(1));

        finalHtml = springTemplateEngine.process("Contract", dataContext);


        documentGenerator.htmlToPdf(finalHtml);

        return "redirect:/admin/vacancyHr/"+idVacancy+"/"+idHr;
    }

    private void checkAdmin(){
        if(adminService.getAdmins().isEmpty()) adminService.initializeAdmin();
    }
}
