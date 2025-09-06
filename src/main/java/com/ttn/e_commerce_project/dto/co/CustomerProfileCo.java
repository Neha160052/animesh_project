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

     @ValidName(message = "{first.name.invalid}")
     String firstName;
     @ValidName(message = "{last.name.invalid}")
     String lastName;
     @Pattern(regexp = PHONE_REGEX, message = "{phone.number.invalid}")
     String contact;
}
