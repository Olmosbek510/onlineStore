package uz.inha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uz.inha.entity.Role;
import uz.inha.entity.User;
import uz.inha.repo.RoleRepo;
import uz.inha.repo.UserRepo;
import uz.inha.repo.UserRoleRepo1;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserRepo userRepo;
    private final UserRoleRepo1 userRoleRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("login")
    @PreAuthorize("!hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public String login() {
        return "auth/login";
    }

    @PreAuthorize("!hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PreAuthorize("!hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("registerPost")
    public String registerPost(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UUID userId = userRepo.save(user);
        Role role = roleRepo.findByCode("ROLE_USER");
        userRoleRepo.save(userId, role.getId());
        return "redirect:/";
    }
}
