package cn.spider.framework.host.application.base.util;

public class PluginKeyUtil {
    public static String buildPluginKey(String bizName,String version){
        return bizName + "#" + version;
    }
}
