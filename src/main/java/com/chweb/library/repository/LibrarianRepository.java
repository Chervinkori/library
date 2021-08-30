package com.chweb.library.repository;

import com.chweb.library.entity.LibrarianEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<LibrarianEntity, Long> {
    Optional<LibrarianEntity> findByIdAndActiveIsTrue(Long id);

    Page<LibrarianEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<LibrarianEntity> findAllByActiveIsTrue();
}