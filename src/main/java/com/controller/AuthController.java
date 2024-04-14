package com.controller;

import com.dto.UserDto;
import com.model.Background;
import com.model.Candidate;
import com.model.UserDetail;
import com.service.BackgroundService;
import com.service.CandidateService;
import com.service.ImageService;
import com.service.UserDetailService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

@Controller
public class AuthController {
    private UserDetailService userDetailService;

    private ImageService imageService;
    private PasswordEncoder passwordEncoder;

    private CandidateService candidateService;

    private BackgroundService backgroundService;

    public AuthController(BackgroundService backgroundService,CandidateService candidateService,UserDetailService userDetailService,ImageService imageService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder=passwordEncoder;
        this.imageService=imageService;
        this.candidateService=candidateService;
        this.backgroundService=backgroundService;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String getCurrentUsernameRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role=auth.getAuthorities().toString();
        role=role.replace('[',' ');
        role=role.replace(']',' ');
        role=role.trim();
        return role;
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register.html";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,BindingResult result, Model model)  {
        if(checkEmptinessEmailPassword(userDto,model)) return "/register";
        if(checkEmail(userDto,model)) return "/register";
        if(checkPassword(userDto,model)) return "/register";
        UserDetail existingUser = userDetailService.findUserByEmail(userDto.getEmail());
        if(checkUserExisting(userDto,existingUser,model)) return "/register";
        return "redirect:/register/end/"+userDto.getEmail()+"/"+userDto.getPassword();
    }

    @GetMapping("/register/end/{email}/{password}")
    public String continueRegistration(@PathVariable("email") String email,@PathVariable("password") String password, Model model){
        // create model object to store form data
        UserDto user=new UserDto();
        model.addAttribute("user", user);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        return "registerEnd.html";
    }

    @PostMapping("/register/end")
    public String endRegistration(String email, String password,UserDto user,@RequestParam("file1") MultipartFile file1, BindingResult result, Model model) throws IOException {
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        //create model object to store form data
        if(checkEmptinessUser(user,email,password,dateOfBirth,model)){return "registerEnd.html";}
        if(checkUserAge(user,email,password,dateOfBirth,model)) {return "registerEnd.html";}
        if(checkExistingPhone(user,email,password,model)){return "registerEnd.html";}
        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "redirect:/register/end/"+email+"/"+password;
        }
        if(user.getPatronymic().isEmpty()){user.setPatronymic("-");}
        if(user.getInfo().isEmpty()){user.setInfo("-");}
        user.setPassword(password);
        user.setFile1(file1);
        user.setEmail(email);
        UserDetail userDetail=userDetailService.saveUser(user,"ROLE_CANDIDATE");
        Candidate candidate=new Candidate();
        Background background=new Background();
        background.setExperience(0);
        background=backgroundService.addAndUpdateBackground(background);
        candidate.setUserDetail(userDetail);
        candidate.setBackground(background);
        candidateService.addAndUpdateCandidate(candidate);
        return "redirect:/login";
    }

    @GetMapping("/roles")
    public String users(Model model){
        if(getCurrentUsernameRoles().equals("ROLE_CANDIDATE")){
            return "redirect:/candidate/candidatePage";
        }
        else if(getCurrentUsernameRoles().equals("ROLE_ADMIN")){
            return "redirect:/admin/adminPage";
        }
        else if(getCurrentUsernameRoles().equals("ROLE_EMPLOYEE")){
            return "redirect:/employee/employeePage";
        }
        else {
            return "redirect:/hr/hrPage";
        }
    }

    public boolean checkUserExisting(UserDto userDto,UserDetail existingUser, Model model){
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            model.addAttribute("user",userDto);
            model.addAttribute("userExist","userExist");
            return true;
        }
        return false;
    }
    public boolean checkPassword(UserDto userDto, Model model){
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
        if (!(PASSWORD_PATTERN.matcher(userDto.getPassword()).matches())){
            model.addAttribute("user",userDto);
            model.addAttribute("wrongPassword","wrongPassword");
            return true;
        }
        return false;
    }
    public boolean checkEmail(UserDto userDto, Model model){
        final String PASSWORD_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(userDto.getEmail()).matches())){
            model.addAttribute("user",userDto);
            model.addAttribute("wrongEmail","wrongEmail");
            return true;
        }
        return false;
    }
    public boolean checkEmptinessEmailPassword(UserDto userDto, Model model){
        if (userDto.getPassword().equals("")|| userDto.getEmail().equals("")){
            model.addAttribute("user",userDto);
            model.addAttribute("empty","empty");
            return true;
        }
        return false;
    }
    public boolean checkEmptinessUser(UserDto user,String email, String password, String dateOfBirth, Model model){
        if(user.getName().equals("") || user.getSurname().equals("") ||  dateOfBirth.equals("") || user.getPhone().equals("")) {
            model.addAttribute("fail", "fail");
            model.addAttribute("user", user);
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return true;
        }
        return false;
    }

    public boolean checkExistingPhone(UserDto user,String email, String password, Model model){
        if(userDetailService.findUserByPhone(user.getPhone())!=null) {
            model.addAttribute("existPhone", "existPhone");
            model.addAttribute("user", user);
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return true;
        }
        return false;
    }
    public boolean checkUserAge(UserDto user,String email, String password, String dateOfBirth, Model model) {
        int age= userDetailService.calculateAge(dateOfBirth);
        if(age<18) {
            model.addAttribute("age", "age");
            model.addAttribute("user", user);
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return true;
        }
        return false;
    }

}

