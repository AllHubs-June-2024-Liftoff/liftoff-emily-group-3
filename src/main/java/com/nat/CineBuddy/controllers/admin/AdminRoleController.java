package com.nat.CineBuddy.controllers.admin;

import com.nat.CineBuddy.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
