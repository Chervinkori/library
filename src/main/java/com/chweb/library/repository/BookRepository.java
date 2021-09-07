package com.chweb.library.repository;

import com.chweb.library.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByIdAndActiveIsTrue(Long id);

    Optional<BookEntity> findByIdAndInStockAndActiveIsTrue(Long id, Boolean inStock);

    Page<BookEntity> findAllByActiveIsTrue(Pageable pageable);

    Page<BookEntity> findAllByInStockAndActiveIsTrue(Pageable pageable, Boolean inStock);

    Collection<BookEntity> findAllByActiveIsTrue();

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :id AND b.active = true")
    Page<BookEntity> findAllByAuthorIdAndActiveIsTrue(Pageable pageable, @Param("id") Long id);

    @Query("SELECT b FROM Book b JOIN b.themes t WHERE t.id = :id AND b.active = true")
    Page<BookEntity> findAllByThemeIdAndActiveIsTrue(Pageable pageable, @Param("id") Long id);

    @Query("SELECT b FROM Book b JOIN b.publishingHouse ph WHERE ph.id = :id AND b.active = true")
    Page<BookEntity> findAllByPublishingHouseIdAndActiveIsTrue(Pageable pageable, @Param("id") Long id);
}