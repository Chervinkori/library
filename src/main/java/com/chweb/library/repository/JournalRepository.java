package com.chweb.library.repository;

import com.chweb.library.entity.JournalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntity, Long> {
    Optional<JournalEntity> findByIdAndActiveIsTrue(Long id);

    Page<JournalEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<JournalEntity> findAllByActiveIsTrue();
}