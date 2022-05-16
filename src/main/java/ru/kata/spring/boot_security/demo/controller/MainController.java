package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.security.Principal;


@Controller
//@RequestMapping(value = "/admin")
public class MainController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


//    @PostConstruct
//    void AddUsers() {
//
//        userService.add(new User((long) 1, "admin@admin.ru", "$2a$12$5/LJTcY5BTW/1dfKXfkONu9SiZbiZfAy2B41V2MuIVrimLrY4ew0K", "admin", 32));
//        userService.add(new User((long) 2, "user@user.ru", "$2a$12$T3fRruYVddVDEv/6kbYOhuKSMp4YKx/YvjYiYvtL.QjiEF2rNIZuy", "user", 68));
//    }

    @GetMapping("/user")
    public String userInfo(Principal principal, Model model) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/")
    public String adminPage() {
        return "redirect:/index";
    }

    @GetMapping(value = "/index")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.listOfAllUsers());
        return "index";
    }

    @RequestMapping(value = "/addNewUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-info";
    }

    @RequestMapping(value = "/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/index";

    }

    @RequestMapping(value = "updateInfo")
    public String updateUser(@RequestParam("id") int id, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getUserById(id));
        return "user-update";
    }

    @RequestMapping(value = "updateUser")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/index";
    }

    @RequestMapping(value = "deleteUser")
    public String deleteUser(@RequestParam("id") int id) {
        userService.removeUserById(id);
        return "redirect:/index";
    }
}
