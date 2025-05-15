package com.ghtk.auction.service;


import com.ghtk.auction.dto.request.product.ProductCreationRequest;
import com.ghtk.auction.dto.request.product.ProductFilterRequest;
import com.ghtk.auction.dto.response.product.ProductResponse;
import com.ghtk.auction.dto.response.product.ProductListResponse;
import com.ghtk.auction.dto.response.user.PageResponse;
import com.ghtk.auction.entity.Product;
import com.ghtk.auction.enums.ProductCategory;
import com.ghtk.auction.enums.ProductStatus;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ProductService {
	
	Product createProduct(Jwt principal, ProductCreationRequest request);
	
	List<ProductResponse> getAllMyProduct(int pageNo, int pageSize);
	PageResponse<ProductResponse> getAllMyProductPagination(int pageNo, int pageSize,String status);
	
	ProductResponse getById(Long id);
	
	List<ProductResponse> getMyByCategory(Jwt principle, ProductFilterRequest category);
	
	ProductResponse deleteProduct(Jwt principal, Long id);
	
	void interestProduct(Jwt principal, Long id);
	
	//so luong
	Long getInterestProduct(Long id);
	
	List<ProductResponse> getMyInterestProduct(Jwt principal);
	
	List<ProductResponse> searchProductbyCategory(ProductFilterRequest request);

	PageResponse<ProductListResponse> searchProduct(String key, int pageNo, int pageSize);

	List<ProductResponse> getTopMostPopularProducts(Long limit);
	
	PageResponse<ProductResponse> getAllProductByCategory(ProductCategory category, ProductStatus status, int pageNo, int pageSize);
	
	PageResponse<ProductResponse> getAllProduct(int pageNo, int pageSize,ProductStatus status);

	List<Integer> listFavoriteProduct(Jwt principal);
	Integer getMyPendingProductsCount(Jwt principal);

	String approvedProduct(Long productId);
}