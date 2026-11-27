package br.org.apae.api.common.util;

public final class LikePattern {
    public static final char ESCAPE_CHARACTER = '\\';

    private LikePattern() {
    }

    public static String contains(String text) {
        return "%" + escape(text) + "%";
    }

    public static String escape(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
