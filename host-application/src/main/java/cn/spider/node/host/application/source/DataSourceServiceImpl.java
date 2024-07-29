package cn.spider.node.host.application.source;

import cn.spider.framework.host.application.base.host.mysql.DataSourceService;

import javax.sql.DataSource;

public class DataSourceServiceImpl implements DataSourceService {
    private SourceManager sourceManager;

    public DataSourceServiceImpl(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
    }

    @Override
    public DataSource queryDataSource(String dataSourceId) {
        return sourceManager.queryDataSource(dataSourceId);
    }
}
