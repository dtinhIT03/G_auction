package com.ghtk.auction.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Filter {
    @JsonIgnore
    private Integer index;
    private String name;
    private Object value;
    private String operation = Operator.EQUAL.getOperator();
    private String type; //date
}
