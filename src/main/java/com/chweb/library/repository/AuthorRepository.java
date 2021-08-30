package com.chweb.library.repository;

import com.chweb.library.entity.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByIdAndActiveIsTrue(Long id);

    Page<AuthorEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<AuthorEntity> findAllByActiveIsTrue();
}