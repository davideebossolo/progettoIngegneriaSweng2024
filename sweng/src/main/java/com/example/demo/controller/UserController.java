package com.example.demo.controller;

import com.example.demo.model.PremiumUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/registration")
    public String processRegistration(
            @RequestParam("userType") String userType,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("firstName") String firstName,
            @RequestParam(value = "cardHolder", required = false) String cardHolder,
            @RequestParam(value = "cardNumber", required = false) String cardNumber,
            @RequestParam(value = "expireDate", required = false) String expireDate,
            @RequestParam(value = "cvv", required = false) String cvv) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        if ("premium".equals(userType)) {
            PremiumUser premiumUser = new PremiumUser();
            premiumUser.setUsername(username);
            premiumUser.setPassword(encodedPassword);
            premiumUser.setEmail(email);
            premiumUser.setFirstName(firstName);
            premiumUser.setCardHolder(cardHolder);
            premiumUser.setCardNumber(cardNumber);
            premiumUser.setExpireDate(expireDate);
            premiumUser.setCvv(cvv);
            userRepository.save(premiumUser);
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            user.setFirstName(firstName);
            userRepository.save(user);
        }

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listAllUsers = userRepository.findAll();
        model.addAttribute("list", listAllUsers);
        return "users_list";
    }

    @GetMapping("/login")
    public String userLogin() {
        return "login";
    }
}
