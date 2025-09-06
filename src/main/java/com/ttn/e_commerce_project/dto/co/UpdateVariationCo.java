package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateVariationCo {

    Long variationId;

    Long quantityAvailable;

    @Positive(message = "variation.price.range")
    Double price;

    String metadata;

    MultipartFile primaryImage;

    List<MultipartFile> secondaryImage;

    Boolean isActive;
}
