package com.ghtk.auction.dto.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@SuppressWarnings("java:S115")
public class Order {
    private String property;
    private String direction;

    public Order() {
    }

    public Order(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }

    public enum Direction {
        asc, desc
    }
}
