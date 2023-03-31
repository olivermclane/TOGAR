package edu.carroll.cs389application.service;

import java.util.regex.Pattern;

public class SanizationService {
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern SQL_PATTERN = Pattern.compile("('|--|insert|select|delete|update|union|create|drop|alter)");

    public static String sanitize(String input) {
        // Remove HTML tags
        String sanitized = HTML_PATTERN.matcher(input).replaceAll("");

        // Remove SQL keywords
        sanitized = SQL_PATTERN.matcher(sanitized).replaceAll("");

        return sanitized;
    }
}
