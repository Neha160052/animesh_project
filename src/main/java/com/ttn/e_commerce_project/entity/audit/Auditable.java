package com.ttn.e_commerce_project.entity.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@FieldDefaults(level= AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Auditable {

    @CreatedDate
    LocalDateTime dateCreated;
    @LastModifiedDate
    LocalDateTime lastUpdated;
    @CreatedBy
    String createdBy;
    @LastModifiedBy
    String updatedBy;

}
