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
     * 领域功能版本的id
     */
    private Integer baseInfoId;
}
