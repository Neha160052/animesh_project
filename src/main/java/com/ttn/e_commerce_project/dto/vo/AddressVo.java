package com.ttn.e_commerce_project.dto.vo;

import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressVo {
    String city;
    String state;
    String country;
    String addressLine;
    String zipCode;
    @Enumerated(EnumType.STRING)
    Label label;
}
