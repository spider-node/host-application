package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

import java.util.List;

@Data
public class QueryBaseInfoResult {
    private List<AreaDomainInfo> domainInfoList;
}
