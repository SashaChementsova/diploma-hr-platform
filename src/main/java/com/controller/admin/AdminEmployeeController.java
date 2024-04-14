package com.controller.admin;

import com.dto.DataString;
import com.dto.Password;
import com.dto.UserDto;
import com.model.*;
import com.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class AdminEmployeeController {
    EmployeeService employeeService;
    HrService hrService;
    RoleService roleService;
    ImageService imageService;
    UserDetailService userDetailService;
    PositionNameService positionNameService;
    LevelPositionService levelPositionService;
    PositionService positionService;
    CityCompanyService cityCompanyService;
    BackgroundService backgroundService;

    LanguageService languageService;
    EducationService educationService;

    public AdminEmployeeController(EducationService educationService,LanguageService languageService,RoleService roleService, EmployeeService employeeService, HrService hrService, ImageService imageService, UserDetailService userDetailService, PositionNameService positionNameService, LevelPositionService levelPositionService, PositionService positionService, CityCompanyService cityCompanyService,BackgroundService backgroundService) {
        this.employeeService = employeeService;
        this.hrService = hrService;
        this.imageService = imageService;
        this.userDetailService = userDetailService;
        this.positionNameService = positionNameService;
        this.levelPositionService = levelPositionService;
        this.positionService = positionService;
        this.cityCompanyService = cityCompanyService;
        this.roleService=roleService;
        this.backgroundService=backgroundService;
        this.languageService=languageService;
        this.educationService=educationService;
    }

    @GetMapping("/admin/employees")
    public String getEmployees(DataString SNP,Model model){
        checkPositions();
        checkCityAndAddress();
        if(SNP.getData()==null){
            model.addAttribute("employees",employeeService.getEmployees());
            model.addAttribute("SNP",new DataString());
            if(employeeService.getEmployees().isEmpty()) model.addAttribute("emptiness","empty");
        }
        else{
            SNP.setData(SNP.getData().trim());
            List<Employee>employees=employeeService.findEmployeeBySNP(SNP.getData());
            model.addAttribute("employees", employees);
            model.addAttribute("SNP",SNP);
            if(employees.isEmpty()) model.addAttribute("emptiness","empty");
        }
        return "admin/employeeControl/getEmployees.html";
    }

    @GetMapping("/admin/employees/{id}")
    public String getEmployees(@PathVariable("id") String id, DataString SNP, Model model){
        checkPositions();
        checkCityAndAddress();
        model.addAttribute("position",positionNameService.findPositionNameById(Integer.parseInt(id)));
        if(SNP.getData()==null){
            model.addAttribute("employees",employeeService.getEmployeesByPositionName(positionNameService.findPositionNameById(Integer.parseInt(id))));
            model.addAttribute("SNP",new DataString());
            if(employeeService.getEmployeesByPositionName(positionNameService.findPositionNameById(Integer.parseInt(id))).isEmpty()) model.addAttribute("emptiness","empty");
        }
        else{
            SNP.setData(SNP.getData().trim());
            List<Employee>employees=employeeService.findEmployeeBySNP(SNP.getData(),positionNameService.findPositionNameById(Integer.parseInt(id)));
            model.addAttribute("employees", employees);
            model.addAttribute("SNP",SNP);
            if(employees.isEmpty()) model.addAttribute("emptiness","empty");
        }
        return "admin/employeeControl/getEmployeesOfPosition.html";
    }

    @GetMapping("/admin/employeeOfPosition/{id}")
    public String getEmployeeOfPosition(@PathVariable("id") String idEmployee, Model model){
        checkPositions();
        checkCityAndAddress();
        model.addAttribute("employee", employeeService.findEmployeeById(Integer.parseInt(idEmployee)));
        return "admin/employeeControl/getEmployeeOfPosition.html";
    }


    @GetMapping("/admin/employee/{id}")
    public String getEmployee(@PathVariable("id") String idEmployee, Model model){
        checkPositions();
        checkCityAndAddress();
        model.addAttribute("employee", employeeService.findEmployeeById(Integer.parseInt(idEmployee)));
        return "admin/employeeControl/getEmployee.html";
    }

//    @GetMapping("/admin/addEmployee/start")
//    public String addEmployee(Model model){
//        addEmployeeModel(model);
//        model.addAttribute("positions", positionNameService.getPositionNames());
//        return "admin/employeeControl/addEmployee.html";
//    }

    @GetMapping("/admin/addEmployee/start/{id}")
    public String addEmployeePosition(@PathVariable("id") String id, Model model){
        addEmployeeModel(model);
        model.addAttribute("position", positionNameService.findPositionNameById(Integer.parseInt(id)));
        return "admin/employeeControl/addEmployeeOfPosition.html";
    }

    @PostMapping("/admin/addEmployee/end/{id}")
    public String addEmployeeEnd(@PathVariable("id")String id, UserDetail user, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model, BindingResult result) throws IOException {
        System.out.println("post method "+id);
        checkPositions();
        checkCityAndAddress();
        if(checkEmptinessUser(user,positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee,model)) return "admin/employeeControl/addEmployeeOfPosition.html";
        if(checkEmail(user,positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee,model)) return "admin/employeeControl/addEmployeeOfPosition.html";
        if(checkPassword(user, positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee,model)) return "admin/employeeControl/addEmployeeOfPosition.html";
        if(checkExistingPhone(user,positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee,model)){return "admin/employeeControl/addEmployeeOfPosition.html";}
        UserDetail existingUser = userDetailService.findUserByEmail(user.getEmail());
        if(checkUserExisting(user,positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee,existingUser,model)) return "admin/employeeControl/addEmployeeOfPosition.html";
        if(checkUserAge(user, positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee,model)) {return "admin/employeeControl/addEmployeeOfPosition.html";}
        if(checkEmployeeDates(user, positionNameService.findPositionNameById(Integer.parseInt(id)), levelPosition, cityCompany,employee, model)) {return "admin/employeeControl/addEmployeeOfPosition.html";}
        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "admin/employeeControl/addEmployeeOfPosition.html";
        }

        if(user.getPatronymic().isEmpty()){user.setPatronymic("-");}
        if(user.getInfo().isEmpty()){user.setInfo("-");}
        user=userDetailService.saveUser(user,"ROLE_EMPLOYEE");
        Position position=new Position();
        position.setLevelPosition(levelPositionService.findLevelPositionById(levelPosition.getIdLevelPosition()));
        position.setPositionName(positionNameService.findPositionNameById(Integer.parseInt(id)));
        List<Position> positions=positionService.getPositions();
        for(Position position1:positions){
            if(position1.getPositionName().getIdPositionName()==position.getPositionName().getIdPositionName()&&position1.getLevelPosition().getIdLevelPosition()==position.getLevelPosition().getIdLevelPosition()){
                position.setIdPosition(position1.getIdPosition());
                break;
            }
        }
        position=positionService.addAndUpdatePosition(position);
        if(position.getPositionName().getName().equals("HR-менеджер")){
            Hr hr=hrService.findHrByUserDetail(user);
            if(hr==null){
                hr=new Hr();
                Role role=roleService.checkAndFindHrRole();
                List<Role> roles=new ArrayList<>(user.getRoles());
                roles.add(role);
                user.setRoles(roles);
                hr.setUserDetail(user);
                hrService.addAndUpdateHr(hr);
                userDetailService.updateUser(user);
            }
        }
        Background background=new Background(); background.setExperience(0);
        backgroundService.addAndUpdateBackground(background);
        employee.setBackground(background);
        employee.setCityCompany(cityCompanyService.findCityCompanyById(cityCompany.getIdCityCompany()));
        employee.setPosition(position);
        employee.setUserDetail(user);
        employeeService.addAndUpdateEmployee(employee);
        return "redirect:/admin/employees/"+id;
    }

    @GetMapping("/admin/editEmployee/{id}")
    public String editEmployeePosition(@PathVariable("id") String id, Model model){
        checkPositions();
        checkCityAndAddress();
        Employee employee=employeeService.findEmployeeById(Integer.parseInt(id));
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("positions", positionNameService.getPositionNames());
        model.addAttribute("cities",cityCompanyService.getCityCompanies());
        model.addAttribute("employee",employee);

        return "admin/employeeControl/editEmployeeOfPosition.html";
    }

    @PostMapping("/admin/editEmployeePos/end")
    public String editEmployeeEndPosition( Employee employee, Model model, BindingResult result) throws IOException {
        //System.out.println(user.getIdUserDetails()+" "+positionName.getIdPositionName()+" "+levelPosition.getIdLevelPosition()+" "+cityCompany.getIdCityCompany());
        checkPositions();
        checkCityAndAddress();
        UserDetail user=employee.getUserDetail();
        if(checkEmptinessUser(user,positionNameService.findPositionNameById(employee.getPosition().getPositionName().getIdPositionName()), employee.getPosition().getLevelPosition(), employee.getCityCompany(),employee,model))  return "admin/employeeControl/editEmployeeOfPosition.html";
        if(checkEmailEditEmployee(user, positionNameService.findPositionNameById(employee.getPosition().getPositionName().getIdPositionName()), employee.getPosition().getLevelPosition(),  employee.getCityCompany(),employee,model))  return "admin/employeeControl/editEmployeeOfPosition.html";
        if (checkPhoneEditEmployee(user, positionNameService.findPositionNameById(employee.getPosition().getPositionName().getIdPositionName()), employee.getPosition().getLevelPosition(),  employee.getCityCompany(),employee,model))  return "admin/employeeControl/editEmployeeOfPosition.html";
        if(checkUserAge(user, positionNameService.findPositionNameById(employee.getPosition().getPositionName().getIdPositionName()), employee.getPosition().getLevelPosition(),  employee.getCityCompany(),employee,model))  return "admin/employeeControl/editEmployeeOfPosition.html";
        if(checkEmployeeDates(user, positionNameService.findPositionNameById(employee.getPosition().getPositionName().getIdPositionName()), employee.getPosition().getLevelPosition(),  employee.getCityCompany(),employee, model))  return "admin/employeeControl/editEmployeeOfPosition.html";
        if(result.hasErrors()){
            model.addAttribute("user", user);
            System.out.println("dededededede");
            return "admin/employeeControl/editEmployeeOfPosition.html";
        }

        System.out.println(employee.getPosition().getPositionName().getIdPositionName()+" "+employee.getPosition().getLevelPosition().getIdLevelPosition());
        Employee employee1=employeeService.findEmployeeById(employee.getIdEmployee());
        Position position=employee1.getPosition();
        position.setLevelPosition(levelPositionService.findLevelPositionById(employee.getPosition().getLevelPosition().getIdLevelPosition()));
        position.setPositionName(positionNameService.findPositionNameById(employee.getPosition().getPositionName().getIdPositionName()));
        List<Position> positions=positionService.getPositions();
        for(Position position1:positions){
            if(position1.getPositionName().getIdPositionName()==position.getPositionName().getIdPositionName()&&position1.getLevelPosition().getIdLevelPosition()==position.getLevelPosition().getIdLevelPosition()){
                position.setIdPosition(position1.getIdPosition());
                break;
            }
        }
        position=positionService.addAndUpdatePosition(position);
        if(position.getPositionName().getName().equals("HR-менеджер")){
            Hr hr=hrService.findHrByUserDetail(user);
            if(hr==null){
                hr=new Hr();
                Role role=roleService.checkAndFindHrRole();
                List<Role> roles=new ArrayList<>(user.getRoles());
                roles.add(role);
                user.setRoles(roles);
                hr.setUserDetail(user);
                hrService.addAndUpdateHr(hr);
            }
        }
        if(user.getPatronymic().isEmpty()){user.setPatronymic("-");}
        if(user.getInfo().isEmpty()){user.setInfo("-");}
        userDetailService.updateUser(user);
        employee1.setCityCompany(cityCompanyService.findCityCompanyById(employee.getCityCompany().getIdCityCompany()));
        employee1.setPosition(position);
        employee1.setUserDetail(user);
        employeeService.addAndUpdateEmployee(employee1);
        return "redirect:/admin/employees/"+employee.getPosition().getPositionName().getIdPositionName();
    }






    @GetMapping("/admin/editEmployeePassword/{id}")
    public String editEmployeePassword(@PathVariable("id") String id, Model model){
        checkPositions();
        checkCityAndAddress();
        Password password=new Password();
        password.setIdUser(employeeService.findEmployeeById(Integer.parseInt(id)).getUserDetail().getIdUserDetails());
        model.addAttribute("password",password);
        model.addAttribute("idPosition", employeeService.findEmployeeById(Integer.parseInt(id)).getPosition().getPositionName().getIdPositionName());
        return "admin/employeeControl/editEmployeePassword.html";
    }

    @PostMapping("/admin/editEmployeePassword/end/{id}")
    public String editEmployeePositionEnd(@PathVariable("id") String id, Password password, Model model, BindingResult result) throws IOException {
        //System.out.println(user.getIdUserDetails()+" "+positionName.getIdPositionName()+" "+levelPosition.getIdLevelPosition()+" "+cityCompany.getIdCityCompany());
        checkPositions();
        checkCityAndAddress();
        if(checkPasswords(password,id,model)) return "admin/employeeControl/editEmployeePassword.html";
        userDetailService.savePassword(userDetailService.findUserById(password.getIdUser()),password.getNewPassword());
        return "redirect:/admin/employees/"+Integer.parseInt(id);
    }

    private void checkPositions(){
        if(positionNameService.getPositionNames().isEmpty()) positionNameService.initializePositionName();
        if(levelPositionService.getLevelPositions().isEmpty()) levelPositionService.initializeLevelPosition();
    }
    private void checkCityAndAddress(){
        if(cityCompanyService.getCityCompanies().isEmpty()){
            cityCompanyService.initializeCityCompany();
        }
    }

    public boolean checkEmailEditEmployee(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model) {
        if(checkEmail(user,positionName,levelPosition,cityCompany,employee,model)) return true;
        UserDetail userDetail=userDetailService.findUserById(user.getIdUserDetails());
        if(userDetail.getEmail().equals(user.getEmail())) return false;
        userDetail=userDetailService.findUserByEmail(user.getEmail());
        return checkUserExisting(user, positionName, levelPosition, cityCompany, employee, userDetail, model);
    }

    public boolean checkPhoneEditEmployee(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model) {
        UserDetail userDetail=userDetailService.findUserById(user.getIdUserDetails());
        if(userDetail.getPhone().equals(user.getPhone())) return false;
        return checkExistingPhone(user, positionName, levelPosition, cityCompany, employee, model);
    }
    public boolean checkUserExisting(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, UserDetail existingUser, Model model){
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            model.addAttribute("userExist","userExist");
            model.addAttribute("positions", positionNameService.getPositionNames());
            model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
            model.addAttribute("cities",cityCompanyService.getCityCompanies());
            model.addAttribute("user",user);
            model.addAttribute("levelPosition",levelPosition);
            model.addAttribute("position",positionName);
            model.addAttribute("city",cityCompany);
            model.addAttribute("employee",employee);
            return true;
        }
        return false;
    }
    public boolean checkPassword(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model){
        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
//        ^                  # start of the string
//        (?=.*[0-9])        # a digit must occur at least once
//                (?=.*[a-z])        # a lower case letter must occur at least once
//        (?=.*[A-Z])        # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])   # a special character must occur at least once
//        (?=\\S+$)          # no whitespace allowed in the entire string
//                .{8,16}            # 8-16 character password, both inclusive
//        $                  # end of the string
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(user.getPassword()).matches())){
            model.addAttribute("positions", positionNameService.getPositionNames());
            model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
            model.addAttribute("cities",cityCompanyService.getCityCompanies());
            model.addAttribute("user",user);
            model.addAttribute("levelPosition",levelPosition);
            model.addAttribute("position",positionName);
            model.addAttribute("city",cityCompany);
            model.addAttribute("employee",employee);
            model.addAttribute("wrongPassword","wrongPassword");
            return true;
        }
        return false;
    }
    public boolean checkEmail(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model){
        final String PASSWORD_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(user.getEmail()).matches())){
            model.addAttribute("positions", positionNameService.getPositionNames());
            model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
            model.addAttribute("cities",cityCompanyService.getCityCompanies());
            model.addAttribute("user",user);
            model.addAttribute("levelPosition",levelPosition);
            model.addAttribute("position",positionName);
            model.addAttribute("city",cityCompany);
            model.addAttribute("employee",employee);
            model.addAttribute("wrongEmail","wrongEmail");
            return true;
        }
        return false;
    }
    public boolean checkEmptinessUser(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model){
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        if (user.getPassword().equals("")|| user.getEmail().equals("") ||user.getName().equals("") || user.getSurname().equals("") ||  dateOfBirth.equals("") || user.getPhone().equals("")){
            model.addAttribute("positions", positionNameService.getPositionNames());
            model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
            model.addAttribute("cities",cityCompanyService.getCityCompanies());
            model.addAttribute("user",user);
            model.addAttribute("levelPosition",levelPosition);
            model.addAttribute("position",positionName);
            model.addAttribute("city",cityCompany);
            model.addAttribute("employee",employee);
            model.addAttribute("empty","empty");
            return true;
        }
        return false;
    }
    public boolean checkUserAge(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model) {
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        int age= userDetailService.calculateAge(dateOfBirth);
        if(age<18) {
            model.addAttribute("age", "age");
            model.addAttribute("positions", positionNameService.getPositionNames());
            model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
            model.addAttribute("cities",cityCompanyService.getCityCompanies());
            model.addAttribute("user",user);
            model.addAttribute("levelPosition",levelPosition);
            model.addAttribute("position",positionName);
            model.addAttribute("city",cityCompany);
            model.addAttribute("employee",employee);
            return true;
        }
        return false;
    }
    public boolean checkExistingPhone(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model){
        if(userDetailService.findUserByPhone(user.getPhone())!=null) {
            model.addAttribute("existPhone", "existPhone");
            model.addAttribute("positions", positionNameService.getPositionNames());
            model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
            model.addAttribute("cities",cityCompanyService.getCityCompanies());
            model.addAttribute("user",user);
            model.addAttribute("levelPosition",levelPosition);
            model.addAttribute("position",positionName);
            model.addAttribute("city",cityCompany);
            model.addAttribute("employee",employee);
            return true;
        }
        return false;
    }
    public boolean checkEmployeeDates(UserDetail user, PositionName positionName, LevelPosition levelPosition,CityCompany cityCompany, Employee employee, Model model) {
        model.addAttribute("positions", positionNameService.getPositionNames());
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("cities",cityCompanyService.getCityCompanies());
        model.addAttribute("user",user);
        model.addAttribute("levelPosition",levelPosition);
        model.addAttribute("position",positionName);
        model.addAttribute("city",cityCompany);
        model.addAttribute("employee",employee);
        int res1=employeeService.compareDates(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateRecruitment()),new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateContractEnd()));
        if(res1==0){
            model.addAttribute("equalDates","equalDates");
            return true;
        }
        int res2=employeeService.compareDates(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateRecruitment()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        if(res2>0){
            model.addAttribute("failDateBegin","failDateBegin");
            return true;
        }
        int res3=employeeService.compareDates(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateContractEnd()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        if(res3<0){
            model.addAttribute("failDateEnd","failDateEnd");
            return true;
        }
        int res4=employeeService.calculateDifferenceDates(employee);
        System.out.println("res4 " +res4);
        if(!(1<=res4&&res4<=5)){
            model.addAttribute("diffDates","diffDates");
            return true;
        }
        return false;
    }
    public void addEmployeeModel(Model model){
        checkPositions();
        checkCityAndAddress();
        model.addAttribute("levelPositions", levelPositionService.getLevelPositions());
        model.addAttribute("cities",cityCompanyService.getCityCompanies());
        model.addAttribute("user",new UserDetail());
        model.addAttribute("levelPosition",new LevelPosition());
        model.addAttribute("position",new PositionName());
        model.addAttribute("city",new CityCompany());
        model.addAttribute("employee",new Employee());
    }

    public boolean checkPasswords(Password password,String id,Model model){
        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
        model.addAttribute("password",password);
        model.addAttribute("idPosition", id);
        if(password.getNewPassword().isEmpty()||password.getOldPassword().isEmpty()||password.getNewPasswordRepeat().isEmpty()){
            model.addAttribute("empty","empty");
            return true;
        }
//        ^                  # start of the string
//        (?=.*[0-9])        # a digit must occur at least once
//                (?=.*[a-z])        # a lower case letter must occur at least once
//        (?=.*[A-Z])        # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])   # a special character must occur at least once
//        (?=\\S+$)          # no whitespace allowed in the entire string
//                .{8,16}            # 8-16 character password, both inclusive
//        $                  # end of the string
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(password.getOldPassword()).matches())){
            model.addAttribute("wrongOldPassword","wrongOldPassword");
            return true;
        }
        if(!(PASSWORD_PATTERN.matcher(password.getNewPassword()).matches())){
            model.addAttribute("wrongNewPassword","wrongNewPassword");
            return true;
        }
        if(!(userDetailService.checkPassword(userDetailService.findUserById(password.getIdUser()),password.getOldPassword()))){
            model.addAttribute("wrongOldUserPassword","wrongOldUserPassword");
            return true;
        }
        if(!(password.getNewPassword().equals(password.getNewPasswordRepeat()))){
            model.addAttribute("notEqual","notEqual");
            return true;
        }
        return false;
    }

    @GetMapping("/admin/deleteEmployee/{id}/{idEmployee}")
    public String deleteEmployee(@PathVariable("id") String id,@PathVariable("idEmployee") String idEmployee, Model model){
        checkPositions();
        checkCityAndAddress();
        model.addAttribute("employees",employeeService.getEmployeesByPositionName(positionNameService.findPositionNameById(Integer.parseInt(id))));
        model.addAttribute("position",positionNameService.findPositionNameById(Integer.parseInt(id)));
        UserDto user=new UserDto();
        user.setId(Integer.parseInt(idEmployee));
        model.addAttribute("employee",user);
        model.addAttribute("delete","delete");
        model.addAttribute("SNP",new DataString());
        return "admin/employeeControl/getEmployeesOfPosition.html";
    }

    @PostMapping("/admin/deleteEmployee/end/{id}")
    public String deleteEmployeeEnd(@PathVariable("id") String id, UserDto employee,Model model){
        if(checkEmailDelete(id,employee,model)) return "admin/employeeControl/getEmployeesOfPosition.html";
        Employee employee1=employeeService.findEmployeeById(employee.getId());
        UserDetail userDetail=userDetailService.findUserById(employee1.getUserDetail().getIdUserDetails());
        Hr hr=hrService.findHrByUserDetail(userDetail);
        if(hr!=null){
            hrService.deleteHr(hr.getIdHr());
        }
        Position position=positionService.findPositionById(employee1.getPosition().getIdPosition());
        List<Employee> employees=position.getEmployeeEntities();
        employees.remove(employee1);
        position.setEmployeeEntities(employees);
        positionService.addAndUpdatePosition(position);
        hrService.deleteHrByUserDetail(employee1.getUserDetail());
        Background background=employee1.getBackground();
        List<Education> educations=employee1.getEducationEntities();
        List<Language> languages=employee1.getLanguages();
        backgroundService.deleteBackground(background.getIdBackground());
        educationService.deleteEducations(educations);
        languageService.deleteLanguages(languages);
        employeeService.deleteEmployeesByPositionName(employee1.getPosition().getPositionName());
        userDetailService.deleteUser(userDetail.getIdUserDetails());
        return "redirect:/admin/employees/"+id;
    }


    public boolean checkEmailDelete(String id,UserDto employee,Model model ){
        final String PASSWORD_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        model.addAttribute("employees", employeeService.getEmployeesByPositionName(positionNameService.findPositionNameById(Integer.parseInt(id))));
        model.addAttribute("position",positionNameService.findPositionNameById(Integer.parseInt(id)));
        model.addAttribute("employee",employee);
        model.addAttribute("delete","deleteEmployee");
        if(employee.getEmail().isEmpty()){
            model.addAttribute("emptyEmail","emptyEmail");
            return true;
        }
        if (!(PASSWORD_PATTERN.matcher(employee.getEmail()).matches())){
            model.addAttribute("wrongEmail","wrongEmail");
            return true;
        }
        Employee employee1=employeeService.findEmployeeById(employee.getId());
        if(!(employee1.getUserDetail().getEmail().equals(employee.getEmail()))){
            model.addAttribute("notEqualEmail","notEqualEmail");
            return true;
        }
        if(!(employee1.getInterviewEntities().isEmpty())){
            model.addAttribute("interviewExist","interviewExist");
            return true;
        }
        Hr hr=hrService.findHrByUserDetail(employee1.getUserDetail());
        if(hr!=null&&!(hr.getVacancyEntities().isEmpty())){
            model.addAttribute("vacancyExist","vacancyExist");
            return true;
        }
        return false;
    }

}
