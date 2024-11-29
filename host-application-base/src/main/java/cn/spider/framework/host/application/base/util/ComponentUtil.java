package cn.spider.framework.host.application.base.util;

public class ComponentUtil {
    public static String buildComponentKey(String componentName,String taskServiceName,String version){
        return componentName + "_" + taskServiceName + "_" + version;
    }
}
