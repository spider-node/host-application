package cn.spider.node.host.application.event;

import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.util.PluginKeyUtil;
import com.alipay.sofa.ark.spi.event.biz.AfterBizStopEvent;
import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 容器停止之后的处理
 */
@Slf4j
@Component
public class AfterBizStopEventHandler implements EventHandler<AfterBizStopEvent> {

    @Resource
    private HostService hostService;

    @Override
    public void handleEvent(AfterBizStopEvent afterBizStopEvent) {
        Biz source = afterBizStopEvent.getSource();
        // 获取到model的版本信息等
        String bizName = source.getBizName();
        String bizVersion = source.getBizVersion();
        log.info("bizName {},bizVersion {}",bizName,bizVersion);
        // 进行宿主机卸载，并且上报spider-node
        hostService.unloadPlugin(PluginKeyUtil.buildPluginKey(bizName,bizVersion));
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
