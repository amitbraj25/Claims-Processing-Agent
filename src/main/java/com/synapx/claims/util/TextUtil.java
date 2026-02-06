package com.synapx.claims.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    public static String clean(String text) {
        if (text == null) return "";
        return text
                .replaceAll("[\\r\\t]+", " ")
                .replaceAll(" +", " ")
                .replaceAll("\\n+", "\n")
                .trim();
    }

    public static String find(String text, String... patterns) {

        if (text == null) return null;

        for (String p : patterns) {
            Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String val = matcher.group(1);
                if (val != null) {
                    val = val.trim();
                    // prevent huge extraction
                    if (val.length() > 200) val = val.substring(0, 200);
                    return val;
                }
            }
        }

        return null;
    }

    public static Double toDouble(String num) {
        if (num == null) return null;
        try {
            String cleaned = num.replace(",", "").trim();
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return null;
        }
    }
}

