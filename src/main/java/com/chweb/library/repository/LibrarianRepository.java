package com.chweb.library.repository;

import com.chweb.library.entity.LibrarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibrarianRepository extends JpaRepository<LibrarianEntity, Long> {
}