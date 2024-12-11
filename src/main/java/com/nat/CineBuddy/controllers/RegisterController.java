package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.services.RoleService;
import com.nat.CineBuddy.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequestMapping("register")
@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String registerForm(Model model){
        model.addAttribute("user",new UserRegistrationDTO());
        model.addAttribute("roles", roleService.getAllRoles());
        return "register";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String submitRegister(@ModelAttribute @Valid UserRegistrationDTO userRegistrationDTO, Errors errors, Model model){
        if(!errors.hasErrors()){
            boolean success = userService.registerUser(userRegistrationDTO);
            if(success){
                return "login";
            }
            else{
                model.addAttribute("user",new UserRegistrationDTO());
                model.addAttribute("roles", roleService.getAllRoles());
                return "register";
            }
        }
        else{
            return "register";
        }
    }
}
