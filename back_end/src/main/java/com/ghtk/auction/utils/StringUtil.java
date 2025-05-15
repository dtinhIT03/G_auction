package com.ghtk.auction.utils;


import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {

    private final SecureRandom RD = new SecureRandom();

    private static final DecimalFormat decimalFormat = new DecimalFormat("000000");


    public static String removeAccent(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D")
                .replaceAll("`", "")
                .replaceAll("´", "")
                .replaceAll("\\^", "");
    }

    public String camelToSnake(String str) {

        // Empty String
        StringBuilder result = new StringBuilder();

        // Append first character(in lower case)
        // to result string
        char c = str.charAt(0);
        result.append(Character.toLowerCase(c));

        // Traverse the string from
        // ist index to last index
        for (int i = 1; i < str.length(); i++) {

            char ch = str.charAt(i);

            // Check if the character is upper case
            // then append '_' and such character
            // (in lower case) to result string
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            }

            // If the character is lower case then
            // add such character into result string
            else {
                result.append(ch);
            }
        }

        // return the result
        return result.toString();
    }

    public String generateRandomStr(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        return RD.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public String generateOTP() {
        return decimalFormat.format(RD.nextInt(999999));
    }

    public String generateOrderCode() {
        return decimalFormat.format(RD.nextInt(999999999));
    }

    public static String buildLikeOperator(String value) {
        if (value == null || "".equals(value.trim())) return null;
        return "%".concat(value.trim().toUpperCase()).concat("%");
    }

    public static String buildLikeOperatorLower(String value) {
        if (value == null || "".equals(value.trim())) return null;
        return "%".concat(value.trim().toLowerCase()).concat("%");
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static String safeToString(Object obj) {
        if (isNull(obj)) {
            return "";
        }
        return String.valueOf(obj);
    }

    public static String safeToString(String obj) {
        if (isBlank(obj)) {
            return null;
        }
        return obj;
    }

    public static String normalizeString(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        String namePart = lastDotIndex != -1 ? filename.substring(0, lastDotIndex) : filename;
        String extensionPart = lastDotIndex != -1 ? filename.substring(lastDotIndex) : "";

        // Bước 1: Bỏ dấu tiếng Việt
        String normalized = removeAccent(namePart);

        // Bước 2: Thay khoảng trắng bằng gạch dưới
        normalized = normalized.replaceAll("[\\s.]+", "_");

        // Bước 3: Loại bỏ ký tự đặc biệt, chỉ giữ chữ cái, số, và dấu gạch dưới
        normalized = normalized.replaceAll("[^a-zA-Z0-9_]", "");

        // Bước 4 (tùy chọn): Chuyển hết thành chữ thường
        normalized = normalized.toLowerCase();

        return normalized + extensionPart;
    }
}