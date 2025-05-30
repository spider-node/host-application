package cn.spider.framework.host.application.base.plugin.data;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TaskRequest {
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
     * 链路id
     */
    private String requestId;

    /**
     * 事务id
     */
    private String xid;

    /**
     * 事务组id
     */
    private Long branchId;

    /**
     * 插件版本
     */
    private String version;

    /**
     * 请求参数
     */
    private Map<String, Object> param;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDomainFunctionKey() {
        String base = this.componentName + "@" +  this.serviceName;
        if(StringUtils.isEmpty(this.version)){
            return base;
        }
        return base + "@" +  version;
    }
}
