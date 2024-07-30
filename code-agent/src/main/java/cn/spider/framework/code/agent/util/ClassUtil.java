package cn.spider.framework.code.agent.util;

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
}
