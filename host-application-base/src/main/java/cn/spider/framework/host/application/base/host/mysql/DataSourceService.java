package cn.spider.framework.host.application.base.host.mysql;

import javax.sql.DataSource;

public interface DataSourceService {
    DataSource queryDataSource(String dataSourceId);
}
