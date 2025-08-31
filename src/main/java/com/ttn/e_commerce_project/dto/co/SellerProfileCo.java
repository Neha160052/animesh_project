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

    @Size(min = 2, max = 20, message = "{first.name.size}")
    @ValidName(message = "{first.name.invalid}")
    String firstName;

    @ValidName(message = "{last.name.invalid}")
    @Size(min = 2, max = 20, message = "{last.name.size}")
    String lastName;

    // SELLER
    @Pattern(regexp = "^[0-9]{7,15}$", message = "{company.contact.size}")
    String companyContact;

    @Size(min = 2, max = 60, message = "{company.name.size}")
    String companyName;

    String image;

    // Indian GSTIN (15 chars): 2 digits + 5 letters + 4 digits + 1 letter + 1 alnum + 'Z' + 1 alnum
    @Pattern(regexp = "^\\d{2}[A-Z]{5}\\d{4}[A-Z][1-9A-Z]Z[0-9A-Z]$",
            message = "{gst.invalid}")
    String gst;

}
