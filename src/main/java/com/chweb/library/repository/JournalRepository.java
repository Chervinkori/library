package com.chweb.library.repository;

import com.chweb.library.entity.JournalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntity, Long> {
    Optional<JournalEntity> findByIdAndActiveIsTrue(Long id);

    Page<JournalEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<JournalEntity> findAllByActiveIsTrue();

    @Query("SELECT j FROM Journal j WHERE j.librarian.id = :id AND j.active = true")
    Page<JournalEntity> findAllByLibrarianIdAndActiveIsTrue(Pageable pageable, @Param("id") Long id);

    @Query("SELECT j FROM Journal j WHERE j.subscriber.id = :id AND j.active = true")
    Page<JournalEntity> findAllByAuthorIdAndActiveIsTrue(Pageable pageable, @Param("id") Long id);
}