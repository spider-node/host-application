package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AreaDomainInitParam {
    /**
     * 主领域
     */
    String areaName;
    /**
     * 子领域
     */
    String sonAreaName;
    /**
     * 包名称 自动生成
     */
    String packageName;

    /**
     * 表名称
     */
    String tableName;

    /**
     * 数据源名称
     */
    private String datasource;

    /**
     * 领域id
     */
    private String areaId;

    private Integer sonDomainId;
}
