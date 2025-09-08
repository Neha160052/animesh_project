package com.ttn.e_commerce_project.entity.address;
import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.processing.SQL;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE address SET is_deleted= true WHERE id=?")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String city;
    String state;
    String country;
    String addressLine;
    String zipCode;
    boolean isDeleted;
    @Enumerated(EnumType.STRING)
    Label label;
}
