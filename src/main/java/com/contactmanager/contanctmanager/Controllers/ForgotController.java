package com.contactmanager.contanctmanager.Controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.contanctmanager.Entites.User;
import com.contactmanager.contanctmanager.Helper.Message;
import com.contactmanager.contanctmanager.Repositories.UserRepository;
import com.contactmanager.contanctmanager.Services.EmailSenderService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ForgotController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailSenderService senderService;

    @Autowired
    UserRepository userRepository;

    int randomNumber;
    String user_email;

    @GetMapping("forgot-password")
    public String getForgetPasswordPage() {
        return "normal_user/forgot";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession session) {
        System.out.println(email);

        // List<User> user = userRepository.findAll();
        // System.out.println("THis is user Data"+user);

        User user = userRepository.findUserByUserName(email);
        user_email = email;
        if (user != null) {
            Random random = new Random();
            randomNumber = random.nextInt(1000, 10000);
            senderService.sendEmail(email.trim(), "Forgot Password OTP",
                    "This is a OTP for the Forgot Password in contact Manager :" + randomNumber);

            System.out.println("This is your OTP please check your email that i send on your email : " + randomNumber);
            return "normal_user/verify_otp";
        }

        else {
            session.setAttribute("message", new Message("user with this email does not login", "danger"));
            System.out.println("Email is not exist : ");
            return "normal_user/verify_otp";
        }

    }

    @PostMapping("/match_otp")
    public String verifyOtp(@RequestParam("otp") int otp) {
        System.out.println("this is otp that you entered : " + otp);
        System.out.println("this is random number : " + randomNumber);
        if (randomNumber == otp) {
            System.out.println("right otp mangal banna : ");
            return "redirect:/new_password";
        } else {
            System.out.println("this is a wrong otp  : ");
            return "normal_user/verify_otp";
        }
    }

    @GetMapping("/new_password")
    public String getNewPassword(Model model) {
        model.addAttribute("title", "Add New Passwod");
        model.addAttribute("user", new User());
        return "normal_user/newPassword";
    }

    @PostMapping("/save-newPassword")
    public String saveNewPassword(@RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword, Model model, HttpSession session) {
        System.out.println("password is saved successfully :");
        model.addAttribute("user", new User());
        if (newPassword.equals(confirmPassword)) {
            System.out.println("THis is user email that user want to forget password : " + user_email);
            User user = userRepository.findUserByUserName(user_email);
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            System.out.println("Users Password : " + user.getPassword());
            session.setAttribute("message", new Message("Password is changed succussfully ", "success"));

        }
        session.setAttribute("message", new Message("Password is not match !!", "danger"));
        return "normal_user/newPassword";
    }

    // @EventListener(ApplicationReadyEvent.class)
    // public void sendEmail() {
    // senderService.sendEmail("mangalt.bca2022@ssism.org", "First Email",
    // "This is a email that i send for the demo ");
    // }
}
