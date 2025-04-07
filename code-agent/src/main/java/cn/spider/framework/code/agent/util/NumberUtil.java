package cn.spider.framework.code.agent.util;

public class NumberUtil {
    private static final String[] numberWords = {
            "zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine"
    };

    private static String numberToString(String number) {
        StringBuilder sb = new StringBuilder();
        for (char c : number.toCharArray()) {
            int digit = Character.getNumericValue(c);
            sb.append(numberWords[digit]).append("-");
        }
        return sb.toString().replaceAll("-$", "");
    }

    public static String versionToEnglish(String version) {
        version = version.replaceAll("[^0-9.]", "");
        String[] parts = version.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            result.append(numberToString(part)).append("-");
        }
        return result.toString().replaceAll("-$", "");
    }

    public static String upgradeVersion(String version) {
        String[] parts = version.split("\\.");
        int lastPart = Integer.parseInt(parts[parts.length - 1]);
        lastPart++;
        parts[parts.length - 1] = String.valueOf(lastPart);
        return String.join(".", parts);
    }
}
