package com.contactmanager.contanctmanager.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

// import com.contactmanager.contanctmanager.Entites.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.contanctmanager.Entites.User;
import com.contactmanager.contanctmanager.Helper.Message;
import com.contactmanager.contanctmanager.Repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/main/")
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String getUserPage() {
        return "home";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/do_register")
    public String registerUser(@ModelAttribute("user") User user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
            HttpSession session) {

        try {

            if (!agreement) {
                throw new Exception("You have not agreed the terms and conditions : ");
            }
            // if (result.hasErrors()) {
            // System.out.println("Errors in case of validation --->>>>>
            // "+result.toString());
            // System.out.println("user --->"+user);
            // model.addAttribute("user", user);
            // return "signup";
            // }
            user.setRole("ROLE_USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnable(true);
            System.out.println("Agreement " + agreement);
            System.out.println("User " + user);
            model.addAttribute("user", userRepository.save(user));
            session.setAttribute("message", new Message("Succesfully Registered ", "User Added Succesfully "));
            // session.removeAttribute("message");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));

        }
        return "signup";
    }

    @GetMapping("/login")
    public String getCustumLoginPage(Model model) {
        model.addAttribute("title", "Login Page");
        return "Login";
    }

}
