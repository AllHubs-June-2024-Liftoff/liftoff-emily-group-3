package com.nat.CineBuddy.controllers.admin;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.RoleService;
import com.nat.CineBuddy.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProfileService profileService;

    @GetMapping("users")
    public String index(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user/index";
    }
    @GetMapping("users/create")
    public String createForm(Model model){
        model.addAttribute("user", new UserRegistrationDTO());
        model.addAttribute("roles",roleService.getAllRoles());
        return "admin/user/create";
    }

    @PostMapping("users/create")
    public String submitRegister(@Valid @ModelAttribute("user") UserRegistrationDTO userRegistrationDTO, Errors errors, Model model){
        System.out.println(userRegistrationDTO);
        if(!errors.hasErrors()){
            boolean success = userService.registerUser(userRegistrationDTO);
            if(success){
                model.addAttribute("users", userService.getAllUsers());
                return "admin/user/index";
            }
            else{
                model.addAttribute("user",userRegistrationDTO);
                model.addAttribute("roles", roleService.getAllRoles());
                return "admin/user/create";
            }
        }
        else{
            model.addAttribute("user", userRegistrationDTO);
            model.addAttribute("roles",roleService.getAllRoles());
            return "admin/user/create";
        }
    }

    @GetMapping("users/update/{id}")
    public String editForm(@PathVariable("id") Integer userId, Model model){
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("roles",roleService.getAllRoles());
        return "admin/user/update";
    }

    @PostMapping("users/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") User submittedUser, BindingResult result, @PathVariable("id") Integer userId, Errors errors, Model model){
        if(!errors.hasErrors() && !result.hasErrors()){
            if(submittedUser.getRoles() == null){
                submittedUser.setRoles(new HashSet<>());
            }
            boolean success = userService.updateUser(submittedUser, userId);
            if(success){
                return "redirect:/admin/users";
            }
            else{
                model.addAttribute("user", submittedUser);
                model.addAttribute("roles", roleService.getAllRoles());
                return "admin/user/update";
            }
        }
        else{
            model.addAttribute("user", submittedUser);
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/user/update";
        }
    }

    @GetMapping("users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer userId, Model model, HttpServletRequest request, HttpServletResponse response){
        boolean allowDelete = false;
        if(userService.getCurrentUser().getRoles().contains(roleService.findByName("ROLE_ADMIN"))){
            allowDelete = true;
        }
        if(userService.getCurrentUser().getId().equals(userId)){
            profileService.logoutUser(request,response);
        }
        if(allowDelete){
            userService.deleteUserById(userId);
        }
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user/index";
    }

}
