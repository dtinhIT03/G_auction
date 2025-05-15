package com.ghtk.auction.dto.response.product;

import com.ghtk.auction.dto.response.user.UserResponse;
import com.ghtk.auction.enums.ProductCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ProductV1Response {
    String id;

    String ownerId;

    String name;

    ProductCategory category;

    String description;

    String image;

    Long quantity;

    Long buyerId;

    String status;

    UserResponse ownerResponse;
}
