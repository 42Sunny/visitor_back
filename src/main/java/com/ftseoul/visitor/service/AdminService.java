package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Admin;
import com.ftseoul.visitor.data.AdminRepository;
import com.ftseoul.visitor.data.Role;
import com.ftseoul.visitor.dto.AdminLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {

    @Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private final HttpSession httpSession;

    @Value("${admin.login.id}")
    private String id;

    public Long signUp(AdminLoginDto adminDto) {
        log.info("signUp");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        return adminRepository.save(adminDto.toEntity()).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUserName: " + username);
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (id.equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getKey()));
            log.info("authority add");
        }
        else
            return null;
        httpSession.setAttribute("username", admin.getUsername());
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
