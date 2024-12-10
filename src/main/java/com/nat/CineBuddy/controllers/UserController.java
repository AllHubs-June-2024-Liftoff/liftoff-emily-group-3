package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.services.RoleService;
import com.nat.CineBuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
}
