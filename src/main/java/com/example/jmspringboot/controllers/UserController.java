package com.example.jmspringboot.controllers;

import com.example.jmspringboot.models.User;
import com.example.jmspringboot.service.RoleService;
import com.example.jmspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String getCurrentUserInfo(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "userPanel";
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("newUser", new User());
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("thisUser", thisUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "adminPanel";
    }

    @PostMapping("/admin/create")
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam(value = "index", required = false) Long[] index) {
        if (index != null) {
            for (Long id : index) {
                user.addRole(roleService.findById(id));
            }
        } else {
            user.addRole(roleService.findById(2L));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String update(@ModelAttribute("newUser") User user,
                         @RequestParam(value = "index", required = false) Long[] index) {
        if (index != null) {
            for (Long id : index) {
                user.addRole(roleService.findById(id));
            }
        } else {
            user.addRole(roleService.findById(2L));
        }
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
