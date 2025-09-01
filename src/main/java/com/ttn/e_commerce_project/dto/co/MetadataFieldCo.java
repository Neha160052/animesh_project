package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetadataFieldCo {
    @NotBlank(message = "{name.required}")
    private String name;

}
