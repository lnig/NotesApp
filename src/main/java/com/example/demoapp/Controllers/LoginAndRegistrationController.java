package com.example.demoapp.Controllers;

import com.example.demoapp.Data.Login;
import com.example.demoapp.Data.User;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.Services.LoginsService;
import com.example.demoapp.Services.UsersService;
import com.example.demoapp.Validators.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Login")
public class LoginAndRegistrationController {
    @Autowired
    LoginsService loginsService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UsersService usersService;

    @GetMapping("/GetToLogin")
    public String getToLoginForm(){
        return "redirect:/Login/LoginForm";
    }

    @GetMapping("/LoginForm")
    public String showLoginForm(Model model){
        model.addAttribute("login", new Login());
        return "index";
    }

    @GetMapping("/GetToLoginFormValidation")
    public String getToLoginFormValidation(RedirectAttributes redirectAttributes){
        Login login = loginsService.getAllLogins().get(0);
        redirectAttributes.addFlashAttribute("login", login);
        return "redirect:/Login/LoginFormValidation";
    }

    @GetMapping("/LoginFormValidation")
    public String processLoginForm(@Valid @ModelAttribute("login") Login login, BindingResult result) {
        if(result.hasErrors()){
            result.getAllErrors().forEach(el -> System.out.println(el));
        }
        return "index";
    }

    @GetMapping("/GetToRegistration")
    public String getToRegistrationForm(){
        return "redirect:/Login/RegistrationForm";
    }

    @GetMapping("/RegistrationForm")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "registrationForm";
    }

    @PostMapping("/RegistrationForm")
    public String processRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult result){
        if(result.hasErrors()){
            result.getAllErrors().forEach(el -> System.out.println(el));
            return "registrationForm";
        }
        UsersEntity usersEntity = usersService.mapToUserEntity(user);
        usersService.addUserToDB(usersEntity);

        return "redirect:/Login/LoginForm";
    }


}
