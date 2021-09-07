package com.chweb.library.repository;

import com.chweb.library.entity.JournalItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface JournalItemRepository extends JpaRepository<JournalItemEntity, JournalItemEntity.JournalItemId> {
    Optional<JournalItemEntity> findByIdAndActiveIsTrue(JournalItemEntity.JournalItemId id);

    Page<JournalItemEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<JournalItemEntity> findAllByActiveIsTrue();

    /**
     * Gets all issued active books.
     */
    Collection<JournalItemEntity> findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(Long bookId);

    @Query("SELECT ji FROM JournalItem ji WHERE ji.book.id = :id AND ji.active = true")
    Page<JournalItemEntity> findAllByBookIdByActiveIsTrue(Pageable pageable, @Param("id") Long id);
}