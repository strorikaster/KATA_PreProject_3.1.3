package com.webcrudsecurityboot.controller;

import com.webcrudsecurityboot.model.Role;
import com.webcrudsecurityboot.model.User;
import com.webcrudsecurityboot.service.RoleService;
import com.webcrudsecurityboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
//@RestController
//@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

   // public static final String REST_URL = "/admin";

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping(value = "/")
//    public String start(){
//        return "redirect:/login";
//    }
//
//    @GetMapping(value = "/login")
//    public String login() {
//            return "login";
//    }

    @GetMapping(value = "/admin")
    public String welcome() {
        return "redirect:/admin/all";
    }

    //@GetMapping(value = "admin/all")
    //@RequestMapping(method = RequestMethod.GET)
//    @ResponseBody
//    @GetMapping()
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
    //public String allUsers(@AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("user", user);
//        model.addAttribute("allUsers", userService.getAllUsers());
//        model.addAttribute("roles", roleService.getAllRoles());
//        return "index";

    //}

    @GetMapping(value = "admin/all")
    public String allUsers(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "index";
    }

    @GetMapping("/{id}")
    public User show(@PathVariable("id") Long id) {
//        model.addAttribute("user", userService.show(user.getId()));
//        model.addAttribute("role", roleService.show(user.getId()));
        return userService.show(id);
    }

//    @GetMapping("admin/{id}")
//    public String show(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
//        model.addAttribute("user", userService.show(user.getId()));
//        model.addAttribute("role", roleService.show(user.getId()));
//        return "show";
//    }

    @GetMapping(value = "admin/add")
    public String addUser(Model model,
                          @ModelAttribute("user") User user,
                          @ModelAttribute("role") Role role) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping(value = "admin/add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam("rolesSelected") Long[] rolesId,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "new";
        }
        userService.save(user, rolesId);
        return "redirect:/admin";
    }


    @GetMapping(value = "admin/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    @PatchMapping("admin/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam("rolesSelected") Long[] rolesId

    ) {
        userService.update(user, rolesId);
        return "redirect:/admin/all";
    }

    @DeleteMapping("admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String getUserPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "show";
    }
}
