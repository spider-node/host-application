package cn.spider.framework.code.agent.data;

import lombok.Data;

@Data
public class DeployAreaFunctionParam {
    /**
     * 业务功能类
     */
    private String serviceClass;
    /**
     * 入参类
     */
    private String paramClass;

    /**
     * 返回参数类
     */
    private String resultClass;

    /**
     * 表明名称
     */
    private String tableName;

    /**
     * 领域名称
     */
    private String areaName;

    /**
     * 数据源
     */
    private String datasource;

    /**
     * 子域的版本baseId
     */
    private Integer baseInfoId;

    /**
     * 任务id ai编码的任务id
     */
    private Integer taskId;

    /**
     * 领域功能的版本id
     */
    private String domainFunctionVersionId;

    /**
     * 组件
     */
    private String taskComponent;

    /**
     * 组件功能
     */
    private String taskService;

    /**
     * 版本
     */
    private String version;

    private String mavenPom;
}
