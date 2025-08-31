package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfileCo {

     @ValidName(message = "firstName can only contain alphabets and spaces")
     String firstName;
     @ValidName(message = "Lastname can only contain alphabets and spaces")
     String lastName;
     String contact;
}
