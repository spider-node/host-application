package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

@Data
public class QueryBaseInfoParam {
    /**
     * 子域
     */
    private String sonArea;

    /**
     * 主领域
     */
    private String area;
}
