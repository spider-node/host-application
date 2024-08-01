package cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service;

import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
public interface AreaDatasourceInfoService extends IService<AreaDatasourceInfo> {
    List<String> getTableNames(Integer id);

}
