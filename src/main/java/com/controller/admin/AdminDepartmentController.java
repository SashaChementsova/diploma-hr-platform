package com.controller.admin;

import com.model.*;
import com.service.DepartmentService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminDepartmentController {

    DepartmentService departmentService;


    public AdminDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/admin/departments")
    public String getDepartments(Model model){
        checkDepartments();
        model.addAttribute("departments", departmentService.getDepartments());
        if(departmentService.getDepartments().isEmpty()) model.addAttribute("emptiness","empty");
        return "admin/departmentControl/getDepartments.html";
    }

    @GetMapping("/admin/addDep")
    public String addDepartment(Model model){
        checkDepartments();
        model.addAttribute("department", new Department());
        model.addAttribute("add","add");
        model.addAttribute("departments", departmentService.getDepartments());
        if(departmentService.getDepartments().isEmpty()) model.addAttribute("emptiness","empty");
        return "admin/departmentControl/getDepartments.html";
    }

    @PostMapping("/admin/addDep/end")
    public String addDepartmentEnd(Department department, Model model){
        model.addAttribute("department", department);
        model.addAttribute("add","add");
        model.addAttribute("departments", departmentService.getDepartments());
        if(departmentService.getDepartments().isEmpty()) model.addAttribute("emptiness","empty");
        if(department.getNameDepartment().isEmpty()||department.getNameDepartment().equals("")){
            model.addAttribute("emptyDepartment","emptyDepartment");
            return "admin/departmentControl/getDepartments.html";
        }
        try{
            departmentService.addAndUpdateDepartment(department);
        }
        catch (Exception ex){
            model.addAttribute("fail","fail");
            return "admin/departmentControl/getDepartments.html";
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/editDep/{id}")
    public String editDepartment(@PathVariable("id") String id, Model model){
        checkDepartments();
        model.addAttribute("edit","edit");
        model.addAttribute("department1", departmentService.findDepartmentById(Integer.parseInt(id)));
        model.addAttribute("departments", departmentService.getDepartments());
        return "admin/departmentControl/getDepartments.html";
    }

    @PostMapping("/admin/editDep/end")
    public String editDepartmentEnd(Department department, Model model){
        model.addAttribute("department1", department);
        model.addAttribute("edit","edit");
        model.addAttribute("departments", departmentService.getDepartments());
        if(department.getNameDepartment().isEmpty()||department.getNameDepartment().equals("")){
            model.addAttribute("emptyDepartment","emptyDepartment");
            return "admin/departmentControl/getDepartments.html";
        }
        try {
            departmentService.addAndUpdateDepartment(department);
        }
        catch (Exception ex){
            model.addAttribute("fail1","fail1");
            return "admin/departmentControl/getDepartments.html";
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/deleteDep/{id}")
    public String deleteDepartment(@PathVariable("id")String idDepartment, Model model){
        checkDepartments();
        Department department=new Department();
        department.setIdDepartment(Integer.parseInt(idDepartment));
        model.addAttribute("delete","delete");
        model.addAttribute("department", department);
        model.addAttribute("departments", departmentService.getDepartments());
        return "admin/departmentControl/getDepartments.html";
    }

    @PostMapping("/admin/deleteDep/end")
    public String deleteDepartmentEnd(Department department1, Model model){
        checkDepartments();
        model.addAttribute("delete","delete");
        model.addAttribute("department", department1);
        model.addAttribute("departments", departmentService.getDepartments());
        Department department=departmentService.findDepartmentById(department1.getIdDepartment());
        if(!(department.getNameDepartment().equals(departmentService.findDepartmentById(department.getIdDepartment()).getNameDepartment()))){
            model.addAttribute("notEqual","notEqual");
            return "admin/departmentControl/getDepartments.html";
        }
        if(!(departmentService.checkDepartmentByEmployees(department))){
            model.addAttribute("notEmptyEmployee","notEmptyEmployee");
            return "admin/departmentControl/getDepartments.html";
        }
        if(!(departmentService.checkDepartmentByVacancies(department))){
            model.addAttribute("notEmptyVacancy","notEmptyVacancy");
            return "admin/departmentControl/getDepartments.html";
        }
        if(!(departmentService.checkDepartmentByTestQuestions(department))){
            model.addAttribute("testQuestion","testQuestion");
            return "admin/departmentControl/getDepartments.html";
        }
        departmentService.deleteHrByDepartment(department);
        departmentService.deleteEmployeesByDepartment(department);
        departmentService.deleteTestQuestionByDepartment(department);
        departmentService.deletePositionByDepartment(department);
        departmentService.deletePositionNameByDepartment(department);
        departmentService.deleteDepartment(department.getIdDepartment());
        return "redirect:/admin/departments";
    }

    private void checkDepartments(){
        if(departmentService.getDepartments().isEmpty()) departmentService.initializeDepartment();
    }


}
