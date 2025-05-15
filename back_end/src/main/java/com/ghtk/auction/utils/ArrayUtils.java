package com.ghtk.auction.utils;


import java.sql.Array;
import java.sql.Connection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayUtils {
    public static <T> void forEachWithIndex(List<T> list, BiConsumer<Integer, T> consumer) {
        IntStream.range(0, list.size())
                .forEach(i -> consumer.accept(i, list.get(i)));
    }
    public static <T, R> List<R> mapWithIndex(List<T> list, BiFunction<Integer, T, R> mapper) {
        return IntStream.range(0, list.size())
                .mapToObj(i -> mapper.apply(i, list.get(i)))
                .collect(Collectors.toList());
    }

    public static Array convertToPostgresArray(List<String> files, Connection connection) {
        if (files == null || files.isEmpty()) {
            return null;  // Return empty array if the list is empty
        }

        try {
            return connection.createArrayOf("text", files.toArray());
        } catch (Exception e) {
            return null;
        }
    }
}
