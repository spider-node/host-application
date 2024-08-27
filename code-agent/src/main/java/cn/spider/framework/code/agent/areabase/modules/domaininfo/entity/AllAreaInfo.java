package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import cn.spider.framework.code.agent.areabase.modules.function.entity.AreaDomainFunctionInfo;
import cn.spider.framework.code.agent.areabase.modules.sonarea.entity.SpiderSonArea;
import lombok.Data;

import java.util.List;

@Data
public class AllAreaInfo {
    // 领域-基础数据信息
    private List<AreaDomainInfo> baseInfos;
    // 领域的功能信息
    private List<AreaDomainFunctionInfo> functionInfos;
    // 子域信息
    private List<SpiderSonArea> sonAreas;

}
