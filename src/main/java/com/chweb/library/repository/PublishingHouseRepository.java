package com.chweb.library.repository;

import com.chweb.library.entity.PublishingHouseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PublishingHouseRepository extends JpaRepository<PublishingHouseEntity, Long> {
    Optional<PublishingHouseEntity> findByIdAndActiveIsTrue(Long id);

    Optional<PublishingHouseEntity> findByNameContainsIgnoreCase(String name);

    Page<PublishingHouseEntity> findAllByActiveIsTrue(Pageable pageable);

    Collection<PublishingHouseEntity> findAllByActiveIsTrue();
}