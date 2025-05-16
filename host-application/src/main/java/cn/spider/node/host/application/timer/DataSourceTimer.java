package cn.spider.node.host.application.timer;

import cn.spider.node.host.application.source.SourceManager;
import io.vertx.core.Vertx;

/*** @ClassName DataSourceTimer
 * @Description TODO
 * @Author dds
 * @Date 2025/5/16 14:56
 */
public class DataSourceTimer {
    private Vertx vertx;

    private SourceManager sourceManager;

    public DataSourceTimer(Vertx vertx, SourceManager sourceManager) {
        this.vertx = vertx;
        this.sourceManager = sourceManager;
        this.start();
    }

    // 创建一个定时器10s执行一次
    public void start() {
        vertx.setPeriodic(1000 * 15, handler -> {
            // 定时器执行的代码
            sourceManager.initDataSource();
        });
    }
}
