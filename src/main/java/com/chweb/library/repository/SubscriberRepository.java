package com.chweb.library.repository;

import com.chweb.library.entity.SubscriberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {
    Optional<SubscriberEntity> findByIdAndActiveIsTrue(Long id);

    Page<SubscriberEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<SubscriberEntity> findAllByActiveIsTrue();
}