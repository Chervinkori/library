package com.chweb.library.repository;

import com.chweb.library.entity.JournalBookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface JournalBookRepository extends JpaRepository<JournalBookEntity, JournalBookEntity.JournalBookId> {
    Optional<JournalBookEntity> findByIdAndActiveIsTrue(Long id);

    Page<JournalBookEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<JournalBookEntity> findAllByActiveIsTrue();
}