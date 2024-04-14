package com.controller.admin;

import com.dto.DataString;
import com.model.CityCompany;
import com.service.CityCompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminCityCompanyController {
    CityCompanyService cityCompanyService;

    public AdminCityCompanyController(CityCompanyService cityCompanyService) {
        this.cityCompanyService=cityCompanyService;
    }

    @GetMapping("/admin/cityCompanies")
    public String getCityCompanies(DataString nameCity, Model model){
        checkCityCompanies();
        if(nameCity.getData()==null){
            model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
            model.addAttribute("nameCity",new DataString());
            if(cityCompanyService.getCityCompanies().isEmpty()) model.addAttribute("emptiness","empty");
        }
        else{
            nameCity.setData(nameCity.getData().trim());
            List<CityCompany> cityCompanies=cityCompanyService.findCityCompaniesByNameCity(nameCity.getData());
            model.addAttribute("cityCompanies", cityCompanies);
            model.addAttribute("nameCity",nameCity);
            if(cityCompanies.isEmpty()) model.addAttribute("emptiness","empty");
        }
        return "admin/cityCompanyControl/getCityCompanies.html";
    }

    @GetMapping("/admin/mapCityCompany/{id}")
    public String mapOfCityCompany(@PathVariable("id") String idCityCompany, Model model){
        checkCityCompanies();
        model.addAttribute("cityCompany", cityCompanyService.findCityCompanyById(Integer.parseInt(idCityCompany)));
        model.addAttribute("mapCityCompany","MapCityCompany");
        model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        return "admin/cityCompanyControl/getCityCompanies.html";
    }


    @GetMapping("/admin/addCityCompany")
    public String addCityCompany(Model model){
        checkCityCompanies();
        model.addAttribute("cityCompany", new CityCompany());
        model.addAttribute("add","add");
        model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        if(cityCompanyService.getCityCompanies().isEmpty()) model.addAttribute("emptiness","empty");
        return "admin/cityCompanyControl/getCityCompanies.html";
    }

    @PostMapping("/admin/addCityCompany/end")
    public String addCityCompanyEnd(CityCompany cityCompany, Model model){
        model.addAttribute("cityCompany", cityCompany);
        model.addAttribute("add","add");
        model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        if(cityCompanyService.getCityCompanies().isEmpty()) model.addAttribute("emptiness","empty");
        if(cityCompany.getNameCity().isEmpty()||cityCompany.getNameCity().equals("")){
            model.addAttribute("emptyCityCompany","emptyCityCompany");
            return "admin/cityCompanyControl/getCityCompanies.html";
        }
        try{
           cityCompanyService.addAndUpdateCityCompany(cityCompany);
        }
        catch (Exception ex){
            model.addAttribute("fail","fail");
            return "admin/cityCompanyControl/getCityCompanies.html";
        }
        return "redirect:/admin/cityCompanies";
    }

    @GetMapping("/admin/editCityCompany/{id}")
    public String editCityCompany(@PathVariable("id") String idCityCompany, Model model){
        checkCityCompanies();
        model.addAttribute("edit","edit");
        model.addAttribute("cityCompany",cityCompanyService.findCityCompanyById(Integer.parseInt(idCityCompany)));
        model.addAttribute("cityCompanies", cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        return "admin/cityCompanyControl/getCityCompanies.html";
    }

    @PostMapping("/admin/editCityCompany/end")
    public String editCityCompanyEnd(CityCompany cityCompany, Model model){
        model.addAttribute("cityCompany", cityCompany);
        model.addAttribute("edit","edit");
        model.addAttribute("cityCompanies",cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        if(cityCompany.getNameCity().isEmpty()||cityCompany.getNameCity().equals("")){
            model.addAttribute("empty","empty");
            return "admin/cityCompanyControl/getCityCompanies.html";
        }
        try {
            cityCompanyService.addAndUpdateCityCompany(cityCompany);
        }
        catch (Exception ex){
            model.addAttribute("fail","fail");
            return "admin/cityCompanyControl/getCityCompanies.html";
        }
        return "redirect:/admin/cityCompanies";
    }

    @GetMapping("/admin/deleteCityCompany/{id}")
    public String deleteCityCompany(@PathVariable("id")String idCityCompany, Model model){
        checkCityCompanies();
        CityCompany cityCompany= new CityCompany();
        cityCompany.setIdCityCompany(Integer.parseInt(idCityCompany));
        model.addAttribute("delete","delete");
        model.addAttribute("cityCompany", cityCompany);
        model.addAttribute("cityCompanies", cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        return "admin/cityCompanyControl/getCityCompanies.html";
    }

    @PostMapping("/admin/deleteCityCompany/end")
    public String deleteCityCompanyEnd(CityCompany cityCompany, Model model){
        checkCityCompanies();
        model.addAttribute("delete","delete");
        model.addAttribute("cityCompany", cityCompany);
        model.addAttribute("cityCompanies", cityCompanyService.getCityCompanies());
        model.addAttribute("nameCity",new DataString());
        CityCompany cityCompany1=cityCompanyService.findCityCompanyById(cityCompany.getIdCityCompany());
        if(!(cityCompany.getNameCity().equals(cityCompany1.getNameCity()))){
            model.addAttribute("notEqual","notEqual");
            return "admin/cityCompanyControl/getCityCompanies.html";
        }
        if(cityCompany1.getEmployeeEntities()!=null){
            if(!(cityCompany1.getEmployeeEntities().isEmpty())){
                model.addAttribute("notEmptyEmployee","notEmptyEmployee");
                return "admin/cityCompanyControl/getCityCompanies.html";
            }
        }
        if(cityCompany1.getVacancyEntities()!=null){
            if(!(cityCompany1.getVacancyEntities().isEmpty())){
                model.addAttribute("notEmptyVacancy","notEmptyVacancy");
                return "admin/cityCompanyControl/getCityCompanies.html";
            }
        }
        cityCompanyService.deleteCityCompany(cityCompany1.getIdCityCompany());
        return "redirect:/admin/cityCompanies";
    }

    private void checkCityCompanies(){
        if(cityCompanyService.getCityCompanies().isEmpty()) cityCompanyService.initializeCityCompany();
    }


}
