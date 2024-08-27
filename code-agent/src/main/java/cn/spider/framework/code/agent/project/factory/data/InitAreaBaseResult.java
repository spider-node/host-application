package cn.spider.framework.code.agent.project.factory.data;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.sonarea.entity.SpiderSonArea;
import lombok.Data;

@Data
public class InitAreaBaseResult {
    /**
     * 子领域-内容
     */
    private SpiderSonArea sonArea;

    /**
     * 领域base信息
     */
    private AreaDomainInfo areaDomainInfo;
}
