package com.chweb.library.repository;

import com.chweb.library.entity.PublishingHouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublishingHouseRepository extends JpaRepository<PublishingHouseEntity, Long> {
    Optional<PublishingHouseEntity> findByNameContainsIgnoreCase(String name);
}