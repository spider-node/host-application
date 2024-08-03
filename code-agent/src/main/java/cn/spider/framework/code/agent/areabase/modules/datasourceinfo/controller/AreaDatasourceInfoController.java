package cn.spider.framework.code.agent.areabase.modules.datasourceinfo.controller;

import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.AreaDatasourceInfoService;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
@RestController
@RequestMapping("/areaDatasource")
public class AreaDatasourceInfoController {

    @Resource
    private AreaDatasourceInfoService datasourceInfoService;

    @GetMapping("/getDatasourceList")
    public Wrapper<List<AreaDatasourceInfo>> getDatasourceList() {
        List<AreaDatasourceInfo> datasourceInfoList = datasourceInfoService.getBaseMapper().selectList(null);
        return WrapMapper.ok(datasourceInfoList);
    }

    @GetMapping("/getTableNames")
    public Wrapper<List<String>> getTableNames(Integer id) {
        List<String> tableNames = datasourceInfoService.getTableNames(id);
        return WrapMapper.ok(tableNames);
    }

}
