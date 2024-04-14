package com.controller.admin;

import com.dto.Password;
import com.model.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

@Controller
public class AdminProfileController {
    private  final UserDetailService userDetailService;

    @Autowired
    public AdminProfileController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/admin/getProfile")
    public String getProfileAdmin(Model model){
        UserDetail user=userDetailService.findUserByEmail(getCurrentUsername());
        model.addAttribute("user",user);
        return "admin/profileControl/getProfile.html";
    }

    @GetMapping("/admin/editProfile")
    public String editProfileAdmin(Model model){
        UserDetail userDetail=userDetailService.findUserByEmail(getCurrentUsername());
        model.addAttribute("user",userDetail);
        return "admin/profileControl/editProfile.html";
    }

    @PostMapping("admin/adminEditProfileEnd")
    public String editProfileAdminEnd(UserDetail userDetail, Model model){
        model.addAttribute("user",userDetail);
        if(!(checkUserEmptiness(userDetail,model)))  return "admin/profileControl/editProfile.html";
        if(!(checkEmail(userDetail,model)))  return "admin/profileControl/editProfile.html";
        if(!(checkUserExisting(userDetail,model)))   return "admin/profileControl/editProfile.html";
        if(!(checkUserAge(userDetail,model)))   return "admin/profileControl/editProfile.html";
        UserDetail userDetail1=userDetailService.findUserById(userDetail.getIdUserDetails());
        userDetail.setImage(userDetail1.getImage());
        userDetailService.updateUser(userDetail);
        return "redirect:/admin/getProfile";
    }

    @GetMapping("/admin/changePassword")
    public String changePasswordAdmin(Model model){
        Password password=new Password();
        password.setIdUser(userDetailService.findUserByEmail(getCurrentUsername()).getIdUserDetails());
        model.addAttribute("password",password);
        return "admin/profileControl/changePassword.html";
    }

    @PostMapping("/admin/changePasswordEnd")
    public String changePasswordEnd( Password password,Model model){
        if(checkPasswords(password,model)) return "admin/profileControl/changePassword.html";
        userDetailService.savePassword(userDetailService.findUserById(password.getIdUser()),password.getNewPassword());
        return "redirect:/admin/getProfile";
    }

    public boolean checkUserExisting(UserDetail userDetail,Model model){
        UserDetail userDetail1=userDetailService.findUserByEmail(userDetail.getEmail());
        if(userDetail1.getIdUserDetails()!=userDetail.getIdUserDetails()&&(userDetail1.getEmail().equals(userDetail.getEmail())||userDetail1.getPhone().equals(userDetail.getPhone()))){
            model.addAttribute("existingUser","existingUser");
            return false;
        }
        return true;
    }

    public boolean checkUserEmptiness(UserDetail user,Model model){
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        if (user.getPassword().equals("")||user.getName().equals("") || user.getSurname().equals("") ||  dateOfBirth.equals("") || user.getPhone().equals("")){
            model.addAttribute("empty","empty");
            return false;
        }
        return true;
    }

    public boolean checkUserAge(UserDetail user,Model model){
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        int age= userDetailService.calculateAge(dateOfBirth);
        if(age<18) {
            model.addAttribute("age","age");
            return false;
        }
        return true;
    }

    public boolean checkEmail(UserDetail user,Model model){
        final String PASSWORD_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        if (!(PASSWORD_PATTERN.matcher(user.getEmail()).matches())){
            model.addAttribute("wrongEmail","wrongEmail");
            return false;
        }
        return true;
    }

    public boolean checkPasswords(Password password,Model model){
        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
        model.addAttribute("password",password);
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
}
