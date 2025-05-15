package com.ghtk.auction.mapper;

import com.ghtk.auction.dto.request.product.ProductCreationRequest;
import com.ghtk.auction.dto.response.product.ProductV1Response;
import com.ghtk.auction.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductV1Mapper extends BaseMapper<Product, ProductV1Response, ProductCreationRequest>{
}
