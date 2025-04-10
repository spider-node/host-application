package cn.spider.framework.host.application.base.heart;

/**
 * 插件上报的信息
 */
public class EscalationInfo {
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

    /**
     * pom中的artifactId
     */
    String moduleName;

    /**
     * pom中的version
     */
    String moduleVersion;

    private String bizVersion;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
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

    public String getUniqueId() {
        return this.componentName + this.serviceName + this.moduleVersion;
    }

    public String getBizVersion() {
        return bizVersion;
    }

    public void setBizVersion(String bizVersion) {
        this.bizVersion = bizVersion;
    }
}
