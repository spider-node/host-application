package cn.spider.node.host.application.util;

public class JdbcUtil {
    public static String queryDatabaseName(String jdbcUrl){
        int startIndex = jdbcUrl.indexOf("//") + 2;
        int endIndex = jdbcUrl.indexOf("/", startIndex);
        startIndex = endIndex + 1;
        endIndex = jdbcUrl.indexOf("?", startIndex);
        if (endIndex == -1) {
            endIndex = jdbcUrl.length();
        }

        return jdbcUrl.substring(startIndex, endIndex);
    }
}
