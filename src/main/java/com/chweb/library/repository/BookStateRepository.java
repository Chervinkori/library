package com.chweb.library.repository;

import com.chweb.library.entity.BookStateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookStateRepository extends JpaRepository<BookStateEntity, Long> {
    Optional<BookStateEntity> findByIdAndActiveIsTrue(Long id);

    Optional<BookStateEntity> findByNameContainsIgnoreCase(String name);

    Page<BookStateEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<BookStateEntity> findAllByActiveIsTrue();
}