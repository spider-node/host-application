package cn.spider.framework.host.application.base.util;

public class ComponentUtil {
    public static String buildComponentKey(String componentName,String taskServiceName){
        return componentName + "_" + taskServiceName;
    }
}
