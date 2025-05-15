package com.ghtk.auction.repository;

import com.ghtk.auction.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query(value = """
    SELECT * FROM
    review r WHERE r.product_id = :product_id
    ORDER BY r.id DESC 
    LIMIT :page_size
    OFFSET :offsetpage
    """,nativeQuery = true)
    List<Object[]> getAllByProductId(@Param(value = "product_id") Long productId
            , @Param(value = "offsetpage") int offset,@Param(value = "page_size") int pageSize);

    Integer countAllByProductId(Long productId);
}
