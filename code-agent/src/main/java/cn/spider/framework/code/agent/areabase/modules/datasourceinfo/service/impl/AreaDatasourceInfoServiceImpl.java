package cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.impl;

import cn.hutool.core.lang.Assert;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.mapper.AreaDatasourceInfoMapper;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.AreaDatasourceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
@Service
public class AreaDatasourceInfoServiceImpl extends ServiceImpl<AreaDatasourceInfoMapper, AreaDatasourceInfo> implements AreaDatasourceInfoService {

    @Override
    public List<String> getTableNames(Integer id) {
        AreaDatasourceInfo datasourceInfo = super.getById(id);
        Assert.notNull(datasourceInfo,"当前查询数据源不存在");
        Connection connection = null;
        ResultSet tables = null;
        try {
            // 创建数据库连接
            connection = DriverManager.getConnection(datasourceInfo.getUrl(), datasourceInfo.getName(), datasourceInfo.getPassword());
            // 获取数据库元数据
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取所有表名
            tables = metaData.getTables(null, null, "%", new String[] {"TABLE"});
            List<String> tableNames = new ArrayList<>();
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            return tableNames;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭结果集和连接
            try {
                if (tables != null) {
                    tables.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
