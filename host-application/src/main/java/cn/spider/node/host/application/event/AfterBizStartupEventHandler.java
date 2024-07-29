package cn.spider.node.host.application.event;
import cn.spider.framework.host.application.base.host.TaskProxyManager;
import com.alipay.sofa.ark.spi.event.biz.AfterBizStartupEvent;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class AfterBizStartupEventHandler implements EventHandler<AfterBizStartupEvent> {

    @Resource
    private TaskProxyManager taskProxyManager;

    @Override
    public void handleEvent(AfterBizStartupEvent afterBizStartupEvent) {
        String bizIdentity = afterBizStartupEvent.getSource().getBizName();
        taskProxyManager.register(bizIdentity);
        // 通知spider-client
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
