package org.example.spring.controller;

import org.example.spring.dao.AuthUserDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AuthUserDao authUserDao;

    public AdminController(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @GetMapping("/users/list")
//    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView adminsPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("users", authUserDao.getAllUsers());
        return modelAndView;
    }
}
