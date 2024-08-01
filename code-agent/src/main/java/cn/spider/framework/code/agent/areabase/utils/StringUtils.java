package cn.spider.framework.code.agent.areabase.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class StringUtils {
    public static String convertToCamelCase(String input) {

        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder camelCaseString = new StringBuilder();

        boolean nextCharUpperCase = true;
        for (char currentChar : input.toCharArray()) {
            if (currentChar == '_') {
                nextCharUpperCase = true;
            } else {
                if (nextCharUpperCase) {
                    camelCaseString.append(Character.toUpperCase(currentChar));
                    nextCharUpperCase = false;
                } else {
                    camelCaseString.append(Character.toLowerCase(currentChar));
                }
            }
        }

        // 确保第一个字符大写
        if (camelCaseString.length() > 0) {
            camelCaseString.setCharAt(0, Character.toUpperCase(camelCaseString.charAt(0)));
        }

        return camelCaseString.toString();
    }

    public static void main(String[] args) {
        String input = "spider_area";
        String result = convertToCamelCase(input);
        System.out.println(result); // 输出: SpiderArea
        System.out.println(Paths.get(System.getProperty("user.dir")));
        // 假设这些是从某个地方动态获取的路径部分
        List<String> pathParts = Arrays.asList("git_project", "autoAreaBase", "dynamic", "number", "of", "folders", "file.txt");
        String[] a = pathParts.toArray(new String[0]);
        // 使用Paths.get(String...)的重载方法，这需要我们将List转换为可变参数
        Path path = Paths.get(a[0],Arrays.copyOfRange(a, 1, a.length));
        System.out.println(path);
        System.out.println(System.getProperty("user.dir"));

    }
}
