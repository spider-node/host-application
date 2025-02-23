package cn.spider.framework.code.agent.project.factory.data;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProjectParam {
    /**
     * 业务功能类
     */
    private String serviceClass;
    /**
     * 入参类
     */
    private List<String> paramClass;

    /**
     * 返回参数类
     */
    private List<String> resultClass;

    /**
     * 其他类
     */
    private List<String> otherCode;
    /**
     * 表明年
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
     * 功能描述
     */
    private String desc;

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

    private Set<Integer> baseInfoId;

    private Integer taskId;

    private String domainFunctionVersionId;

    private String mavenPom;
}
