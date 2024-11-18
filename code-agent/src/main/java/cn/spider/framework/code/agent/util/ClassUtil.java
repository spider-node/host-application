package cn.spider.framework.code.agent.util;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.DomainFieldInfo;

import java.util.*;
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
        String[] parts = version.split("\\.");
        int lastIndex = parts.length - 1;
        int lastPart = Integer.parseInt(parts[lastIndex]);

        if (lastPart == 0 && lastIndex > 0 && parts[lastIndex - 1].isEmpty()) {
            // 版本号以"."结尾的情况
            parts = Arrays.copyOf(parts, parts.length - 1);
            lastIndex--;
            lastPart = Integer.parseInt(parts[lastIndex]);
            parts[lastIndex] = String.valueOf(lastPart + 1);
            parts = Arrays.copyOf(parts, parts.length + 1);
            parts[lastIndex + 1] = "0";
        } else {
            parts[lastIndex] = String.valueOf(lastPart + 1);
        }

        return String.join(".", parts);
    }

    public static String getNextVersion(String versionStr) {
        Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");
        Matcher matcher = pattern.matcher(versionStr);

        if (matcher.matches()) {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));
            int patch = Integer.parseInt(matcher.group(3));

            if (patch < 9) {
                patch++;
            } else if (minor < 9) {
                minor++;
                patch = 0;
            } else {
                major++;
                minor = 0;
                patch = 0;
            }

            return String.format("%d.%d.%d", major, minor, patch);
        } else {
            throw new IllegalArgumentException("Invalid version string format");
        }
    }


    public static String updateVersion(String version, UpgradeType upgradeType) {
        String[] parts = version.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);

        switch (upgradeType) {
            case MAJOR:
                major++;
                minor = 0;
                patch = 0;
                break;
            case MINOR:
                minor++;
                patch = 0;
                break;
            case PATCH:
                patch++;
                break;
            default:
                throw new IllegalArgumentException("Unsupported upgrade type: " + upgradeType);
        }
        return String.format("%d.%d.%d", major, minor, patch);
    }

    public static List<FieldInfo> extractFieldsWithComments(String entityClassString) {
        // 正则表达式匹配注释和private字段
        String regex = "(?s)(?:^|\\n)\\s*\\/\\*\\*(.*?)\\*\\/(\\s*private\\s+\\w+\\s+\\w+;)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(entityClassString);
        List<FieldInfo> fields = new ArrayList<>();
        while (matcher.find()) {
            String comment = matcher.group(1).trim(); // 注释
            String fieldInfo = matcher.group(2).trim(); // 字段信息
            String[] parts = fieldInfo.split("\\s+");
            String fieldType = parts[1]; // 类型
            String fieldName = parts[2].replace(";", ""); // 名称
            fields.add(new FieldInfo(fieldName, fieldType, comment));
        }

        return fields;
    }

    /**
     * 从给定的类定义字符串中解析字段信息
     *
     * @param classDefinition 包含类定义的字符串
     * @return 字段名和类型/描述的映射
     */
    public static List<DomainFieldInfo> parseFieldInfo(String classDefinition) {
        Pattern classCommentPattern = Pattern.compile("/\\*\\*.*?\\*/\\s*", Pattern.DOTALL);
        Matcher classCommentMatcher = classCommentPattern.matcher(classDefinition);
        classDefinition = classCommentMatcher.replaceFirst("");

        // 正则表达式匹配字段信息
        Pattern fieldPattern = Pattern.compile(
                "\\s*\\/\\*\\*\\s*\\*\\s*(.*?)\\s*\\*\\/\\s*" + // 注释
                        "(?:@TableId\\(value = \"(.*?)\"(?:, type = (.*?))?\\))?\\s*" + // @TableId 注解
                        "(?:@TableField\\(\"(.*?)\"\\))?\\s*" + // @TableField 注解
                        "private\\s+(\\w+)\\s+(\\w+);", // 字段声明
                Pattern.DOTALL
        );

        Matcher matcher = fieldPattern.matcher(classDefinition);
        List<DomainFieldInfo> domainFieldInfos = new ArrayList<>();
        while (matcher.find()) {
            String description = matcher.group(1).trim();
            String tableIdValue = matcher.group(2);
            String tableFieldName = matcher.group(4);
            String fieldType = matcher.group(5);
            String fieldName = matcher.group(6);
            String tableFieldNameNew = tableFieldName != null ? tableFieldName : tableIdValue != null ? tableIdValue : "";
            DomainFieldInfo domainFieldInfo = new DomainFieldInfo(fieldName, fieldType, description, tableFieldNameNew);
            domainFieldInfos.add(domainFieldInfo);
        }
        return domainFieldInfos;
    }
}
