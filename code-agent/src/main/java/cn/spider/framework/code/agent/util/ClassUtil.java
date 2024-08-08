package cn.spider.framework.code.agent.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassUtil {

    // 获取一个类的，类名称
    public static String extractClassName(String classString) {
        int start = classString.indexOf("class ");
        if (start == -1) {
            start = classString.indexOf("public class ");
        }
        if (start == -1) {
            throw new IllegalArgumentException("Class definition not found in the string.");
        }

        // 跳过 "public class " 或 "class "
        start += ("public class ".length() > start ? "public class ".length() : "class ".length());

        int end = classString.indexOf(' ', start);
        if (end == -1) {
            end = classString.indexOf('{', start);
        }

        if (end == -1) {
            throw new IllegalArgumentException("Class name or opening brace not found after class definition.");
        }

        return classString.substring(start, end).trim();
    }

    public static String camelToSnake(String camelCaseStr) {
        if (camelCaseStr == null || camelCaseStr.isEmpty()) {
            return camelCaseStr;
        }

        // Check if the first character is uppercase and convert it to lowercase.
        char firstChar = camelCaseStr.charAt(0);
        String modifiedStr = Character.isUpperCase(firstChar) ?
                Character.toLowerCase(firstChar) + camelCaseStr.substring(1) :
                camelCaseStr;

        StringBuilder snakeCaseStr = new StringBuilder();
        for (int i = 0; i < modifiedStr.length(); i++) {
            char ch = modifiedStr.charAt(i);

            // If current character is uppercase and it's not the first character,
            // append an underscore before it and change it to lowercase.
            if (Character.isUpperCase(ch) && i > 0) {
                snakeCaseStr.append('_');
                snakeCaseStr.append(Character.toLowerCase(ch));
            } else {
                snakeCaseStr.append(ch);
            }
        }

        return snakeCaseStr.toString();
    }

    public static String toLowerCamelCase(String pascalCaseStr) {
        if (pascalCaseStr == null || pascalCaseStr.isEmpty()) {
            return pascalCaseStr;
        }

        // Check if the first character is uppercase.
        char firstChar = pascalCaseStr.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            // Convert the first character to lowercase and concatenate with the rest of the string.
            return Character.toLowerCase(firstChar) + pascalCaseStr.substring(1);
        } else {
            // The first character is already lowercase, so just return the original string.
            return pascalCaseStr;
        }
    }

    public static String replaceUnderscoresWithSlashes(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Remove trailing underscore if present
        if (input.endsWith("_")) {
            input = input.substring(0, input.length() - 1);
        }

        // Replace underscores with slashes
        return input.replace('_', '/');
    }

    public static String incrementVersion(String version) {
        // 正则表达式匹配版本号的最后一个数字
        Pattern pattern = Pattern.compile("^(.*?)(\\d+)$");
        Matcher matcher = pattern.matcher(version);

        if (matcher.find()) {
            String prefix = matcher.group(1);
            int number = Integer.parseInt(matcher.group(2));
            // 递增数字
            number++;
            // 返回新的版本号
            return prefix + number;
        } else {
            throw new IllegalArgumentException("Invalid version format: " + version);
        }
    }
}
