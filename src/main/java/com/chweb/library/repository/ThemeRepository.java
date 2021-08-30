package com.chweb.library.repository;

import com.chweb.library.entity.ThemeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {
    Optional<ThemeEntity> findByIdAndActiveIsTrue(Long id);

    Optional<ThemeEntity> findByNameContainsIgnoreCase(String name);

    Page<ThemeEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<ThemeEntity> findAllByActiveIsTrue();
}