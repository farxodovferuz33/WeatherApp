package org.example.spring.controller;

import jakarta.validation.Valid;
import org.example.spring.dao.AuthUserDao;
import org.example.spring.dao.CityDao;
import org.example.spring.domain.AuthUser;
import org.example.spring.dto.UserRegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class UserController {
    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;
    private final CityDao cityDao;

    public UserController(AuthUserDao authUserDao, PasswordEncoder passwordEncoder, CityDao cityDao) {
        this.authUserDao = authUserDao;
        this.passwordEncoder = passwordEncoder;
        this.cityDao = cityDao;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/city/list")
    public ModelAndView cityList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user_cities");
        modelAndView.addObject("cities", cityDao.getAllCity());
        return modelAndView;
    }

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/auth/register")
    public String registerPage(Model model) {
        model.addAttribute("dto", new UserRegisterDTO());
        return "register";
    }

    @GetMapping("/auth/logout")
    public String logoutPage() {
        return "logout";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("dto") UserRegisterDTO dto, BindingResult errors) throws IOException {
        if (errors.hasErrors()) return "register";
        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .build();
        authUserDao.save(authUser);
        return "redirect:/auth/login";
    }


//    @GetMapping("/user/image/{username:.+}")
//    public ResponseEntity<Resource> downloadPage(@PathVariable String username) {
//        int i = username.indexOf(".");
//        String substring = username.substring(0,i);
//        Uploads uploads = uploadsDao.findImgByUserName(substring);
//        FileSystemResource fileSystemResource = new FileSystemResource(rootPath.resolve(username));
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(uploads.getMimeType()))
//                .contentLength(uploads.getSize())
//                .header("Content-Disposition", "attachment; filename = " + uploads.getOriginalName())
//                .body(fileSystemResource);
//    }
}
