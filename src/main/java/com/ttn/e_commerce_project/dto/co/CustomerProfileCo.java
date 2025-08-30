package com.ttn.e_commerce_project.dto.co;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfileCo {
     String firstName;
     String lastName;
     String contact;
}
