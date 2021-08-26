package com.chweb.library.repository;

import com.chweb.library.entity.BookStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookStateRepository extends JpaRepository<BookStateEntity, Long> {
    Optional<BookStateEntity> findByNameContainsIgnoreCase(String name);
}