package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import com.ttn.e_commerce_project.entity.address.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerProfileCo {

    @Size(min = 2, max = 50, message = "First name must be 2–50 chars")
    @ValidName(message = "firstName can only contain alphabets and spaces")
    String firstName;

    @ValidName(message = "Lastname can only contain alphabets and spaces")
    @Size(min = 2, max = 50, message = "Last name must be 2–50 chars")
    String lastName;

    // SELLER
    @Pattern(regexp = "^[0-9]{7,15}$", message = "Company contact must be 7–15 digits")
    String companyContact;

    @Size(min = 2, max = 120, message = "Company name must be 2–120 chars")
    String companyName;

    String image;

    // Indian GSTIN (15 chars): 2 digits + 5 letters + 4 digits + 1 letter + 1 alnum + 'Z' + 1 alnum
    @Pattern(regexp = "^\\d{2}[A-Z]{5}\\d{4}[A-Z][1-9A-Z]Z[0-9A-Z]$",
            message = "Invalid GST number")
    String gst;

}
