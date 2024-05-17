package uz.inha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.inha.entity.User;
import uz.inha.repo.UserRepo;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/details")
    @ResponseBody
    public String get(){
        return "user";
    }
    private final UserRepo userRepo;
    @GetMapping("/profile/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String profile(@PathVariable(name = "email" )String email, Model model){
        User user = userRepo.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }
}
