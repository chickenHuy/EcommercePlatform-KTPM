package com.hkteam.ecommerce_platform.dto.request;

import com.hkteam.ecommerce_platform.validator.ValidSpace;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    @ValidSpace
    String name;

    @ValidSpace
    String description;

    Long parentId;
}
