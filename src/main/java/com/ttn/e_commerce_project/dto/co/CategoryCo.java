package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCo {

   @NotBlank(message = "Category name cannot be blank")
   String name;
   Long parentId;
}
