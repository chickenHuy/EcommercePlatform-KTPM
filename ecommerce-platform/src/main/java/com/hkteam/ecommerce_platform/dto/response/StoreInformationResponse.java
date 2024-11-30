package com.hkteam.ecommerce_platform.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreInformationResponse {
    String id;
    String name;
    String bio;
    Float rating;
    String username;
    String defaultAddress;
    Integer totalProduct;
    Instant createdAt;
    String avatarStore;
}