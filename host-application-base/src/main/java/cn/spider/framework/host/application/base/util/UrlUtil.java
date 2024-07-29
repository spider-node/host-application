package cn.spider.framework.host.application.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
    public static String extractJdbcUrl(String jdbcUrl) {
        // 正则表达式匹配 JDBC URL 的前缀和数据库部分
        Pattern pattern = Pattern.compile("jdbc:mysql://[^?]+");
        Matcher matcher = pattern.matcher(jdbcUrl);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
