package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

@Data
public class QueryDomainParam {
    /**
     * 领域名称
     */
    private String areaName;

    /**
     * 子域名称
     */
    private String sonAreaName;
}
