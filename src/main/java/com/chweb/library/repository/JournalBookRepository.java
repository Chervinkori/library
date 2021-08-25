package com.chweb.library.repository;

import com.chweb.library.entity.JournalBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalBookRepository extends JpaRepository<JournalBookEntity, JournalBookEntity.JournalBookId> {
}