package CRUDApplication.controller;

import CRUDApplication.models.Role;
import CRUDApplication.models.User;
import CRUDApplication.service.RoleService;
import CRUDApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/")
public class AdminController {


    private final RoleService roleService;
    private final UserService userService;


    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }



    @GetMapping(value = {"users", "/"})
    public String listUsers(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listUser", this.userService.getAllUsers());
        return "show_users";
    }

    @GetMapping(value = "users/add")
    public String addUser(User user, ModelMap model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping(value = "users/add")
    public String addUser(@RequestParam("roles") Long[] roleId, @ModelAttribute("user") User user) {
        Set<Role> roles = new HashSet<>();
        for (Long rId : roleId) {
            roles.add(roleService.showById(rId));
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin/";
    }


    @PostMapping("/users/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("role") String[] role) {
        Set<Role> roles = new HashSet<>();
        for(String str: role){
            roles.add(roleService.getRoleName(str));
        }
        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin/";
    }


    @GetMapping("/users/remove/{id}")
    public String removeUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        this.userService.delete(user);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "users/{id}/edit")
    public String editUser(@PathVariable("id") Long id, ModelMap model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "update_user";
    }

    @PostMapping(value = "users/{id}/edit")
    public String editUser(@ModelAttribute("user") User user, @PathVariable("id") Long id, @RequestParam("roles") Long[] roleId) {
        Set<Role> roles = new HashSet<>();
        for (Long rId : roleId) {
            roles.add(roleService.showById(rId));
        }
        user.setId(id);
        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin/";
    }
}
