package cn.spider.node.host.application.timer;

import cn.spider.framework.common.utils.ExceptionMessage;
import cn.spider.node.host.application.test.InitComponent;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SystemTimer {

    private Vertx vertx;

    @Resource
    private ApplicationContext applicationContext;

    public SystemTimer() {
        this.vertx = Vertx.vertx();
        delayLoadResource();
    }

    public void delayLoadResource() {
        vertx.setTimer(5000, id -> {
            try {
                InitComponent initComponent = applicationContext.getBean(InitComponent.class);
                initComponent.init();
            } catch (Exception e) {
                log.info("init-fail {}", ExceptionMessage.getStackTrace(e));
            }
        });
    }

}
