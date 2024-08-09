package cn.spider.framework.host.application.base.plugin.task.data;

import java.lang.reflect.Method;

public class SpiderPlugin {
    private Method method;

    private Object tagertObject;

    private String key;

    /**
     * 组件名称
     */
    private String componentName;
    /**
     * service名称
     */
    private String serviceName;
    /**
     * 方法名称
     */
    private String methodName;

    public SpiderPlugin(Method method, Object tagertObject, String key, String componentName, String serviceName, String methodName) {
        this.method = method;
        this.tagertObject = tagertObject;
        this.key = key;
        this.componentName = componentName;
        this.serviceName = serviceName;
        this.methodName = methodName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTagertObject() {
        return tagertObject;
    }

    public void setTagertObject(Object tagertObject) {
        this.tagertObject = tagertObject;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

