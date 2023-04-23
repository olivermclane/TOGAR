package edu.carroll.cs389application.service;

import java.util.regex.Pattern;

/**
 * The SanizationService class provides static methods for sanitizing user input.
 * It contains regular expressions for removing HTML tags and SQL keywords.
 */
public class SanitizationService {

    /**
     * This pattern matches any HTML tag.
     */
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]*>");

    /**
     * This pattern matches any SQL commands.
     */
    private static final Pattern SQL_PATTERN = Pattern.compile("('|--|insert|select|delete|update|union|create|drop|alter)");

    /**
     * Sanitizes the given input string by removing HTML tags and SQL keywords.
     *
     * @param input the string to be sanitized
     * @return the sanitized string
     */
    public static String sanitize(String input) {
        // Remove HTML tags
        String sanitized = HTML_PATTERN.matcher(input).replaceAll("");

        // Remove SQL keywords
        sanitized = SQL_PATTERN.matcher(sanitized).replaceAll("");

        return sanitized;
    }
}
