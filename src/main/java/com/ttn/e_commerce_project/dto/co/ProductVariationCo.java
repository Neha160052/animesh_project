package com.ttn.e_commerce_project.dto.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductVariationCo {

        @NotNull
        Long productId;

        @NotNull
        @Min(value = 0, message = "variation.quantity.range")
        Long quantityAvailable;

        @NotNull(message = "variation.price.notNull")
        @Positive(message = "variation.price.range")
        Double price;

        @NotNull(message = "variation.metadata.notNull")
        String metadata;

        MultipartFile primaryImage;

        List<MultipartFile> secondaryImage;

}
