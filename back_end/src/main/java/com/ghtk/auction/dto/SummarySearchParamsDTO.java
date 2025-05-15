package com.ghtk.auction.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummarySearchParamsDTO {

    private String tableName; // Name of table

    private Class<?> clazz; // clazz response

    private List<ConditionParamsDTO> conditions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConditionParamsDTO {
        @JsonIgnore
        private int index;
        private String whereClause; // Conditions of where

        private List<String> conditionClauses; // Conditions of clauses

        Map<String, Object> parameters; // parameters to conditionClauses

        List<String> conditionClauseQuickSearch; // conditions for quick search

        private String term; // parameters for quickSearch

        private Map<String, Object> parametersForCustom;
    }
}