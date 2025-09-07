package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import com.ttn.e_commerce_project.entity.address.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.ttn.e_commerce_project.constants.UserConstants.*;

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
    @Pattern(regexp = COMPANY_CONTACT, message = "{company.contact.size}")
    String companyContact;

    @Pattern(regexp = COMPANY_NAME_REGEX,message ="{company.name.invalid}")
    @Size(min = 2, max = 60, message = "{company.name.size}")
    String companyName;

    // Indian GSTIN (15 chars): 2 digits + 5 letters + 4 digits + 1 letter + 1 alnum + 'Z' + 1 alnum
    @Pattern(regexp = GST_REGEX,
            message = "{gst.invalid}")
    String gst;

}
