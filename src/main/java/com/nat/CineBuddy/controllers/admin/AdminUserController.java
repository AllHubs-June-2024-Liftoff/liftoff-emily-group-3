package com.nat.CineBuddy.controllers.admin;

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

@Controller
@RequestMapping("admin")
public class AdminUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("users")
    public String index(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user/index";
    }
    @GetMapping("users/create")
    public String createForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roles",roleService.getAllRoles());
        return "admin/user/create";
    }

    @PostMapping("users/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String submitRegister(@ModelAttribute @Valid UserRegistrationDTO userRegistrationDTO, Errors errors, Model model){
        if(!errors.hasErrors()){
            boolean success = userService.registerUser(userRegistrationDTO);
            if(success){
                model.addAttribute("users", userService.getAllUsers());
                return "admin/user/index";
            }
            else{
                model.addAttribute("user",new User());
                model.addAttribute("roles", roleService.getAllRoles());
                return "admin/user/create";
            }
        }
        else{
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
    public String updateUser(@ModelAttribute @Valid User user, @PathVariable("id") Integer userId, Errors errors, Model model){
        if(!errors.hasErrors()){
            boolean success = userService.updateUser(user, userId);
            if(success){
                model.addAttribute("users", userService.getAllUsers());
                return "admin/user/index";
            }
            else{
                model.addAttribute("user", user);
                model.addAttribute("roles", roleService.getAllRoles());
                return "admin/user/update";
            }
        }
        else{
            return "admin/user/update";
        }
    }

    @GetMapping("users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer userId, Model model){
        userService.deleteUserById(userId);
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user/index";
    }
}
