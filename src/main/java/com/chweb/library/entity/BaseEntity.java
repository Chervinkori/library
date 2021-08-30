package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Base entity with parameters for all entities.
 *
 * @author chervinko <br>
 * 18.08.2021
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(nullable = false)
    private Boolean active = true;
}
