package cn.spider.node.host.application.event;

import com.alipay.sofa.ark.spi.event.biz.AfterBizStopEvent;
import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 容器停止之后的处理
 */
@Slf4j
@Component
public class AfterBizStopEventHandler implements EventHandler<AfterBizStopEvent> {

    @Override
    public void handleEvent(AfterBizStopEvent afterBizStopEvent) {
        Biz source = afterBizStopEvent.getSource();
        // 获取到model的版本信息等
        String bizName = source.getBizName();
        String bizVersion = source.getBizVersion();
        log.info("bizName {},bizVersion {}",bizName,bizVersion);
        // 通过 bizName与bizVersion 获取到这个版本中的 taskService等信息
        // 通知 spider 本宿主机拥有的这些能力，进行注销 (下一次调度，就不会调度到这个宿主机上面)

    }

    @Override
    public int getPriority() {
        return 0;
    }
}
