package org.example.spring.controller;

import org.example.spring.dao.AdminDAO;
import org.example.spring.dao.AuthUserDao;
import org.example.spring.dao.CityDao;
import org.example.spring.domain.AuthUser;
import org.example.spring.domain.City;
import org.example.spring.domain.SubscribedCity;
import org.example.spring.domain.Weather;
import org.example.spring.dto.CityDTO;
import org.example.spring.dto.WeatherDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AuthUserDao authUserDao;
    private final CityDao cityDao;
    private final AdminDAO adminDAO;

    public AdminController(AuthUserDao authUserDao, CityDao cityDao, AdminDAO adminDAO) {
        this.authUserDao = authUserDao;
        this.cityDao = cityDao;
        this.adminDAO = adminDAO;
    }

    @GetMapping
    public String adminsPage() {
        return "admin";
    }

    @GetMapping("/users/list")
    public ModelAndView userList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("usersList");
        modelAndView.addObject("users", authUserDao.getAllUsers());
        return modelAndView;
    }

    @GetMapping("/cities/list")
    public ModelAndView cities() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cities");
        modelAndView.addObject("cities", cityDao.getAllCity());
        return modelAndView;
    }

    @GetMapping("/add/city")
    public String addCityPage(Model model) {
        return "adminaddcity";
    }

    @PostMapping("/add/city")
    public String addCity(@ModelAttribute CityDTO cityDTO, Authentication authentication) {
        City city = City.builder().country(cityDTO.country())
                .name(cityDTO.name()).build();
        cityDao.save(city, authentication);
        return "redirect:/admin/cities/list";
    }

    @PostMapping("/delete/city/{id}")
    public String deleteCity(@PathVariable int id) {
        cityDao.deleteCity(id);
        return "redirect:/admin/cities/list";
    }

    @GetMapping("/update/city/{id}")
    public String updatePage(@PathVariable int id, Model model) {
        Optional<City> cityOptional = cityDao.findById(id);
        cityOptional.ifPresent(city -> {
            model.addAttribute("city", city);
        });
        return "updatecity";
    }

    @PostMapping("/update/city/{id}")
    public String updateCity(@PathVariable Integer id, @ModelAttribute CityDTO cityDTO) {
        cityDao.update(id, cityDTO);
        return "redirect:/admin/cities/list";
    }

    @GetMapping("/weather/city/{id}")
    public String getWeatherByCity(@PathVariable int id, Model model) {
        List<Weather> weather = cityDao.getWeather(id);
        model.addAttribute("weathers", weather);
        model.addAttribute("city_id", id);
        return "weather";
    }

    @PostMapping("/delete/weather/{id}")
    public String deleteWeather(@PathVariable int id) {
        Integer i = cityDao.deleteWeather(id);
        System.out.println(i);
        return "redirect:/admin/weather/city/" + i;
    }


    @GetMapping("/add/weather/{id}")
    public String addWeatherPage(@PathVariable int id) {
        return "addweather";
    }

    @PostMapping("/add/weather/{city_id}")
    public String addWeather(@PathVariable int city_id, @ModelAttribute WeatherDTO weatherDTO) {
        Integer i = cityDao.addWeather(city_id, weatherDTO);
        return "redirect:/admin/weather/city/" + i;
    }

    @GetMapping("/users/details/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        List<SubscribedCity> cities = cityDao.getSubscribedCities(id);
        authUserDao.findById(id).ifPresent((authUser)->{
            model.addAttribute("user", authUser);
        });
        model.addAttribute("cities", cities);
        return "userdetail";
    }

    @PostMapping("/deactivate/user")
//    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateUser(@RequestParam("id") Long id) {
        adminDAO.deactivate(id);
        return "redirect:/admin/users/list";
    }

    @PostMapping("/activate/user")
//    @PreAuthorize("hasRole('ADMIN')")
    public String activateUser(@RequestParam("id") Long id) {
        adminDAO.activate(id);
        return "redirect:/admin/users/list";
    }
}
