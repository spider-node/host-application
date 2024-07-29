package cn.spider.framework.host.application.base.host.heart;

import cn.spider.framework.host.application.base.host.data.EscalationInfo;

/**
 * 用于-插件与宿主机通信
 */
public interface HostService {
    // 上报插件信息
    void escalationPlugInInfo(EscalationInfo escalationInfo);
}
