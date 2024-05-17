package uz.inha.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.inha.entity.Role;
import uz.inha.entity.User;
import uz.inha.repo.UserRepo;
import uz.inha.repo.UserRoleRepo1;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserRoleRepo1 userRoleRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        System.out.println(user);
        List<Role> roles = userRoleRepo.findByUser(user.getId());
        user.setRoles(roles);
        return user;
    }
}
