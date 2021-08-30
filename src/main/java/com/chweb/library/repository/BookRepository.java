package com.chweb.library.repository;

import com.chweb.library.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByIdAndActiveIsTrue(Long id);

    Page<BookEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<BookEntity> findAllByActiveIsTrue();
}