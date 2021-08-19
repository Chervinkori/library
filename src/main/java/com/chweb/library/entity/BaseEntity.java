package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
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
    @ColumnDefault("true")
    private Boolean active = false;
}
