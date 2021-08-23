package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Admin;
import com.ftseoul.visitor.data.AdminRepository;
import com.ftseoul.visitor.data.Role;
import com.ftseoul.visitor.dto.AdminLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {

    @Autowired
    private final AdminRepository adminRepository;

    public Long signUp(AdminLoginDto adminDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        return adminRepository.save(adminDto).get().getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getKey()));
        }

        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
