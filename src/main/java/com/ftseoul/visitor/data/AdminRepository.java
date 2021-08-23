package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.AdminLoginDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);

    Optional<Admin> save(AdminLoginDto adminLoginDto);
}
