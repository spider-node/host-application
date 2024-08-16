package cn.spider.node.host.application.area;

import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.linker.client.escalation.AreaInfoService;

/**
 * 查询基座中所有的数据
 */
public class AreaInfoServiceImpl implements AreaInfoService {

    private HostService hostService;

    public AreaInfoServiceImpl(HostService hostService) {
        this.hostService = hostService;
    }

    @Override
    public void escalationAreaInfo() {
        hostService.escalationPlugInAll();
    }
}
