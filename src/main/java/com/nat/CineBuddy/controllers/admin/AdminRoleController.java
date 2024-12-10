package com.nat.CineBuddy.controllers.admin;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin")
public class AdminRoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("roles")
    public String index(Model model){
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/role/index";
    }

    @GetMapping("roles/create")
    public String createForm(Model model){
        model.addAttribute("role",new Role());
        return "admin/role/create";
    }

    @PostMapping("roles/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String submitRegister(@ModelAttribute @Valid Role role, Errors errors, Model model){
        if(!errors.hasErrors()){
            boolean success = roleService.createRole(role);
            if(success){
                model.addAttribute("roles", roleService.getAllRoles());
                return "admin/role/index";
            }
            else{
                model.addAttribute("role",new Role());
                return "admin/role/create";
            }
        }
        else{
            model.addAttribute("role",new Role());
            return "admin/role/create";
        }
    }

    @GetMapping("roles/update/{id}")
    public String createForm(@PathVariable("id") Integer roleId, Model model){
        model.addAttribute("role",roleService.getRoleById(roleId));
        return "admin/role/update";
    }

    @PostMapping("roles/update/{id}")
    public String updateUser(@ModelAttribute @Valid Role role, @PathVariable("id") Integer roleId, Errors errors, Model model){
        if(!errors.hasErrors()){
            boolean success = roleService.updateRole(role, roleId);
            if(success){
                model.addAttribute("roles", roleService.getAllRoles());
                return "admin/role/index";
            }
            else{
                model.addAttribute("role", roleService.getRoleById(roleId));
                return "admin/role/update";
            }
        }
        else{
            return "admin/role/update";
        }
    }

    @GetMapping("roles/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer roleId, Model model){
        roleService.deleteRoleById(roleId);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/role/index";
    }

}
