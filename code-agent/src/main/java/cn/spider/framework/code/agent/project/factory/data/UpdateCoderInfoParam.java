package cn.spider.framework.code.agent.project.factory.data;

import java.util.List;

public class UpdateCoderInfoParam {

    /**
     * 功能版本信息
     */
    private String domainFunctionVersionId;

    /**
     * 业务类信息
     */
    private String areaFunctionClass;

    /**
     * 业务方法的入参
     */
    private List<String> areaFunctionParamClass;

    /**
     * 业务方法的出参
     */
    private List<String> areaFunctionResultClass;

    /**
     * 实例数量
     */
    private Integer instanceNum;

    public String getDomainFunctionVersionId() {
        return domainFunctionVersionId;
    }

    public void setDomainFunctionVersionId(String domainFunctionVersionId) {
        this.domainFunctionVersionId = domainFunctionVersionId;
    }

    public String getAreaFunctionClass() {
        return areaFunctionClass;
    }

    public void setAreaFunctionClass(String areaFunctionClass) {
        this.areaFunctionClass = areaFunctionClass;
    }

    public List<String> getAreaFunctionParamClass() {
        return areaFunctionParamClass;
    }

    public void setAreaFunctionParamClass(List<String> areaFunctionParamClass) {
        this.areaFunctionParamClass = areaFunctionParamClass;
    }

    public List<String> getAreaFunctionResultClass() {
        return areaFunctionResultClass;
    }

    public void setAreaFunctionResultClass(List<String> areaFunctionResultClass) {
        this.areaFunctionResultClass = areaFunctionResultClass;
    }

    public Integer getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(Integer instanceNum) {
        this.instanceNum = instanceNum;
    }
}
