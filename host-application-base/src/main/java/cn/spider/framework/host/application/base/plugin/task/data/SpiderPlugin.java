package cn.spider.framework.host.application.base.plugin.task.data;

import cn.spider.framework.host.application.base.plugin.param.RefreshAreaParam;
import lombok.Data;

import java.lang.reflect.Method;

@Data
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

    private RefreshAreaParam param;

    public SpiderPlugin(Method method, Object tagertObject, String key, String componentName, String serviceName, String methodName) {
        this.method = method;
        this.tagertObject = tagertObject;
        this.key = key;
        this.componentName = componentName;
        this.serviceName = serviceName;
        this.methodName = methodName;
    }
}

