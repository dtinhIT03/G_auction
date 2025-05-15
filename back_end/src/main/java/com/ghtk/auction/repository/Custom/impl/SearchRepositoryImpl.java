//package com.ghtk.auction.repository.Custom.impl;
//
//
//import com.ghtk.auction.dto.QuickSearchParamsDTO;
//import com.ghtk.auction.dto.SummarySearchParamsDTO;
//import com.ghtk.auction.dto.model.SearchRequest;
//import com.ghtk.auction.repository.SearchRepository;
//import com.ghtk.auction.utils.ArrayUtils;
//import com.ghtk.auction.utils.DbMapper;
//import com.ghtk.auction.utils.StringUtil;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import jakarta.persistence.Tuple;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Repository
//@SuppressWarnings("java:S1192")
//public class SearchRepositoryImpl implements SearchRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private final DbMapper dbMapper;
//
//    @Autowired
//    public SearchRepositoryImpl(DbMapper dbMapper) {
//        this.dbMapper = dbMapper;
//    }
//
//    @Override
//    public Page<?> search(SearchRequest request, Class<?> clazz) {
//        ArrayUtils.forEachWithIndex(request.getFilters(), (index, filter) -> {
//            filter.setIndex(index);
//        });
//
//        QuickSearchParamsDTO quickSearchParamsDTO = convertToQuickSearchParamsDTO(request, clazz);
//        return quickSearch(quickSearchParamsDTO);
//    }
//
//    @Override
//    public List<Long> summary(List<SearchRequest> requestList, Class<?> clazz) {
//        SummarySearchParamsDTO summarySearchParamsDTO = convertToSummaryParamsDTO(requestList, clazz);
//        return summaryExec(summarySearchParamsDTO);
//    }
//
//    @SuppressWarnings("java:S1452")
//    public Page<?> quickSearch(QuickSearchParamsDTO quickSearchParamsDTO) {
//        StringBuilder sb = new StringBuilder("WITH ALL_DATA AS (SELECT COUNT(*) OVER() AS TOTAL, ")
//                .append(String.join(AppConstants.CommonSymbol.COMMA, quickSearchParamsDTO.getColumns()))
//                .append(" FROM ")
//                .append(quickSearchParamsDTO.getTableName())
//                .append(" WHERE 1=1 ");
//        if (!StringUtil.isNullOrBlank(quickSearchParamsDTO.getWhereClause())) {
//            sb.append(" AND ").append(quickSearchParamsDTO.getWhereClause());
//        }
//        if (!quickSearchParamsDTO.getConditionClauses().isEmpty()) {
//            sb.append(" AND ")
//                    .append(String.join(" AND ", quickSearchParamsDTO.getConditionClauses()))
//                    .append(" ");
//        }
//        if (!quickSearchParamsDTO.getConditionClauseQuickSearch().isEmpty() && !StringUtil.isNullOrBlank(quickSearchParamsDTO.getTerm())) {
//            sb.append(" AND ( ")
//                    .append(String.join(" OR ", quickSearchParamsDTO.getConditionClauseQuickSearch()))
//                    .append(" ) ");
//        }
//
//        if (!quickSearchParamsDTO.getSortClauses().isEmpty()) {
//            sb.append(" ORDER BY ")
//                    .append(String.join(AppConstants.CommonSymbol.COMMA, quickSearchParamsDTO.getSortClauses()));
//        }
//        sb.append(" ) SELECT * FROM ALL_DATA LIMIT :limit OFFSET :offset ");
//        Query query = this.entityManager.createNativeQuery(sb.toString(), Tuple.class);
//        if (!quickSearchParamsDTO.getConditionClauses().isEmpty()) {
//            quickSearchParamsDTO.getParameters().forEach((key, value) -> {
//                if (Objects.nonNull(value)) {
//                    query.setParameter(key, value);
//                }
//            });
//        }
//        if (!quickSearchParamsDTO.getConditionClauseQuickSearch().isEmpty() && !StringUtil.isNullOrBlank(quickSearchParamsDTO.getTerm())) {
//            query.setParameter(AppConstants.QueryParams.TERM_PARAM, quickSearchParamsDTO.getTerm());
//        }
//        if (Objects.nonNull(quickSearchParamsDTO.getParametersForCustom()) && !quickSearchParamsDTO.getParametersForCustom().isEmpty()) {
//            quickSearchParamsDTO.getParametersForCustom().forEach((key, value) -> {
//                if (Objects.nonNull(value)) {
//                    query.setParameter(key, value);
//                }
//            });
//        }
//
//        query.setParameter(AppConstants.QueryParams.LIMIT_PARAM, quickSearchParamsDTO.getPageable().getPageSize());
//        query.setParameter(AppConstants.QueryParams.OFFSET_PARAM, quickSearchParamsDTO.getPageable().getOffset());
//        List<Tuple> results = query.getResultList();
//        List<?> convertedResults = this.dbMapper.castSqlResult(results, quickSearchParamsDTO.getClazz());
//        long total = results.isEmpty() ? 0 : this.dbMapper.getLongSafe(results.get(0), "TOTAL");
//        return new PageImpl<>(convertedResults, quickSearchParamsDTO.getPageable(), total);
//    }
//
//    @SuppressWarnings("java:S1452")
//    public List<Long> summaryExec(SummarySearchParamsDTO params) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT ");
//
//        List<String> countClauses = new ArrayList<>();
//        Map<String, Object> parameters = new HashMap<>();
//        for (SummarySearchParamsDTO.ConditionParamsDTO condition : params.getConditions()) {
//            int index = condition.getIndex();
//            List<String> whereStr = new ArrayList<>();
//
//            if (!StringUtil.isNullOrBlank(condition.getWhereClause())) {
//                whereStr.add(condition.getWhereClause());
//            }
//
//            if (!condition.getConditionClauseQuickSearch().isEmpty() && !StringUtil.isNullOrBlank(condition.getTerm())) {
//                whereStr.addAll(condition.getConditionClauseQuickSearch());
//            }
//
//            if (!condition.getConditionClauses().isEmpty()) {
//                whereStr.addAll(condition.getConditionClauses());
//            }
//
//            if (!condition.getParameters().isEmpty()) {
//                parameters.putAll(condition.getParameters());
//            }
//
//            if (Objects.nonNull(condition.getParametersForCustom()) && !condition.getParametersForCustom().isEmpty()) {
//                parameters.putAll(condition.getParametersForCustom());
//            }
//
//            if (!condition.getConditionClauseQuickSearch().isEmpty() && !StringUtil.isNullOrBlank(condition.getTerm())) {
//                parameters.put(AppConstants.QueryParams.TERM_PARAM + "_" + index, condition.getTerm());
//            }
//
//            if (whereStr.isEmpty()) {
//                countClauses.add("COUNT(*) AS count_" + index);
//            } else {
//                countClauses.add("COUNT(*) FILTER (WHERE " + whereStr.stream().map(filterCondition -> "(" + filterCondition + ")").collect(Collectors.joining(" AND ")) + ") AS count_" + index);
//            }
//        }
//
//        sql.append(String.join(", ", countClauses));
//        sql.append(" FROM ").append(params.getTableName());
//
//        Query query = entityManager.createNativeQuery(sql.toString());
//
//        if (!parameters.isEmpty()) {
//            parameters.forEach((key, value) -> {
//                if (Objects.nonNull(value)) {
//                    query.setParameter(key, value);
//                }
//            });
//        }
//        Object[] result;
//        if (countClauses.size() > 1) {
//            result = (Object[]) query.getSingleResult();
//        } else if (countClauses.size() == 1) {
//            result = new Object[]{(Object) query.getSingleResult()};
//        } else {
//            return List.of();
//        }
//
//        // Convert result về List<Integer>
//        return Arrays.stream(result)
//                .map(o -> o != null ? ((Number) o).longValue() : 0)
//                .collect(Collectors.toList());
//    }
//
//    private SummarySearchParamsDTO convertToSummaryParamsDTO(List<SearchRequest> requestList, Class<?> clazz) {
//        Map<String, DbMapper.DbFieldHolder> outputMap = new HashMap<>();
//        this.dbMapper.getMethodMap(clazz, outputMap);
//        QuickSearchDomain quickSearchDomain = clazz.getAnnotation(QuickSearchDomain.class);
//        String tableName = quickSearchDomain.tableName().isEmpty() ? clazz.getSimpleName() : quickSearchDomain.tableName();
//
//        List<SummarySearchParamsDTO.ConditionParamsDTO> conditionParamsDTOList = ArrayUtils.mapWithIndex(requestList, (index, request) -> {
//            List<String> conditionClauses = getConditionClauses(request.getFilters());
//            Map<String, Object> parameters = new HashMap<>();
//            request.getFilters().forEach(filter -> {
//                String name = getNameParam(filter);
//                if (!StringUtil.isNullOrBlank(filter.getType()) && filter.getType().equalsIgnoreCase("date")) {
//                    ZonedDateTime zonedDateTime = ZonedDateTime.parse((String) filter.getValue()).withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DateTimeFmt.YYYY_MM_DD_HH_MM_SS_ISO_T);
//                    parameters.put(name, LocalDateTime.parse(formatter.format(zonedDateTime)));
//                } else
//                    parameters.put(name, filter.getValue());
//            });
//
//            List<String> conditionListQuickSearch = new ArrayList<>();
//            this.getConditionList(clazz, conditionListQuickSearch);
//
//            conditionListQuickSearch = conditionListQuickSearch.stream()
//                    .map(str -> str.replace(":term", ":term" + "_" + index))
//                    .collect(Collectors.toList());
//
//            return SummarySearchParamsDTO.ConditionParamsDTO.builder()
//                    .index(index)
//                    .term(request.getKeyword())
//                    .conditionClauses(conditionClauses)
//                    .parameters(parameters)
//                    .conditionClauseQuickSearch(conditionListQuickSearch)
//                    .parametersForCustom(request.getParametersForCustom())
//                    .build();
//        });
//
//        return SummarySearchParamsDTO.builder()
//                .tableName(tableName)
//                .clazz(clazz)
//                .conditions(conditionParamsDTOList)
//                .build();
//    }
//
//    private QuickSearchParamsDTO convertToQuickSearchParamsDTO(SearchRequest request, Class<?> clazz) {
//        Map<String, DbMapper.DbFieldHolder> outputMap = new HashMap<>();
//        this.dbMapper.getMethodMap(clazz, outputMap);
//        QuickSearchDomain quickSearchDomain = clazz.getAnnotation(QuickSearchDomain.class);
//        String tableName = quickSearchDomain.tableName().isEmpty() ? clazz.getSimpleName() : quickSearchDomain.tableName();
//        List<String> orderClauses = request.getSorts().stream()
//                .map(e -> StringUtil.camelToSnake(e.getProperty()) + AppConstants.CommonSymbol.SPACE + e.getDirection())
//                .toList();
//        List<String> conditionClauses = getConditionClauses(request.getFilters());
//        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
//        Map<String, Object> parameters = new HashMap<>();
//        request.getFilters().forEach(filter -> {
//            String name = getNameParam(filter);
//
//            if (!StringUtil.isNullOrBlank(filter.getType()) && filter.getType().equalsIgnoreCase("date")) {
//                ZonedDateTime zonedDateTime = ZonedDateTime.parse((String) filter.getValue()).withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DateTimeFmt.YYYY_MM_DD_HH_MM_SS_ISO_T);
//                parameters.put(name, LocalDateTime.parse(formatter.format(zonedDateTime)));
//            } else
//                parameters.put(name, filter.getValue());
//        });
//        List<String> conditionListQuickSearch = new ArrayList<>();
//        this.getConditionList(clazz, conditionListQuickSearch);
//        return QuickSearchParamsDTO.builder()
//                .term(request.getKeyword())
//                .tableName(tableName)
//                .columns(outputMap.keySet())
//                .conditionClauses(conditionClauses)
//                .parameters(parameters)
//                .conditionClauseQuickSearch(conditionListQuickSearch)
//                .sortClauses(orderClauses)
//                .clazz(clazz)
//                .pageable(pageable)
//                .parametersForCustom(request.getParametersForCustom())
//                .isPermissionData(request.getIsPermissionData())
//                .isEmployee(request.getIsEmployee())
//                .build();
//    }
//
//    private List<String> getConditionClauses(List<Filter> filters) {
//        List<String> conditions = new ArrayList<>();
//        filters.forEach(filter -> {
//            StringBuilder condition = new StringBuilder(StringUtil.camelToSnake(filter.getName()));
//            String paramName = getNameParam(filter);
//            switch (Operator.from(filter.getOperation())) {
//                case EQUAL:
//                    condition.append(" = :").append(paramName);
//                    break;
//                case NOT_EQUAL:
//                    condition.append(" != :").append(paramName);
//                    break;
//                case GREATER_THAN:
//                    condition.append(" > :").append(paramName);
//                    break;
//                case LESS_THAN:
//                    condition.append(" < :").append(paramName);
//                    break;
//                case GREATER_THAN_OR_EQUAL:
//                    condition.append(" >= :").append(paramName);
//                    break;
//                case LESS_THAN_OR_EQUAL:
//                    condition.append(" <= :").append(paramName);
//                    break;
//                case LIKE:
//                    condition.append(" LIKE CONCAT('%',:").append(paramName).append(",'%')");
//                    break;
//                case IN:
//                    condition.append(" IN :").append(paramName);
//                    break;
//                case NIN:
//                    condition.append(" NOT IN :").append(paramName);
//                    break;
//                case IS_NULL:
//                    condition.append(" IS NULL");
//                    break;
//                case IS_NOT_NULL:
//                    condition.append(" IS NOT NULL");
//                    break;
//                default:
//                    // Handle other cases or ignore
//                    break;
//            }
//            conditions.add(condition.toString());
//        });
//        return conditions;
//    }
//
//    private String getNameParam(Filter filter) {
//        Integer index = filter.getIndex();
//        String paramName = filter.getName() + "_" + index;
//        return switch (Operator.from(filter.getOperation())) {
//            case GREATER_THAN, GREATER_THAN_OR_EQUAL -> paramName + "From";
//            case LESS_THAN, LESS_THAN_OR_EQUAL -> paramName + "To";
//            default ->
//                // Handle other cases or ignore
//                    paramName;
//        };
//    }
//
//    protected <R> void getConditionList(Class<R> clazz, List<String> conditionList) {
//        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
//            this.getConditionList(clazz.getSuperclass(), conditionList);
//        }
//
//        for (Field field : clazz.getDeclaredFields()) {
//            QuickSearchInput quickSearchInput = field.getAnnotation(QuickSearchInput.class);
//            if (quickSearchInput == null) {
//                continue;
//            }
//            String cond = quickSearchInput.keyOption().getValue()
//                    .replace("%COLUMN_NAME%", quickSearchInput.columnName());
//            conditionList.add(cond);
//        }
//    }
//}
