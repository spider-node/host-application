package cn.spider.framework.host.application.base.plugin.task.data;

import java.lang.reflect.Method;

public class SpiderPlugin {
    private Method method;

    private Object tagertObject;

    private String key;

    public SpiderPlugin(Method method, Object tagertObject, String key) {
        this.method = method;
        this.tagertObject = tagertObject;
        this.key = key;
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

