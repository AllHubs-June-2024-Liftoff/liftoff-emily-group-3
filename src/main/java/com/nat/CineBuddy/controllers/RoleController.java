package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("roles", roleService.getAllRoles());
        return "role/index";
    }

}