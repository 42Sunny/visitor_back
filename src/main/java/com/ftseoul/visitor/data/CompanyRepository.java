package com.ftseoul.visitor.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByIsDeletedIsFalse();

    Optional<Company> findByName(String name);
}
