package cn.spider.framework.code.agent.areabase.modules.domaininfo.controller;

import cn.hutool.json.JSONUtil;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.AreaDatasourceInfoService;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.*;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.service.AreaDomainInfoService;
import cn.spider.framework.code.agent.areabase.modules.function.entity.AreaDomainFunctionInfo;
import cn.spider.framework.code.agent.areabase.modules.function.service.IAreaDomainFunctionInfoService;
import cn.spider.framework.code.agent.areabase.modules.sonarea.entity.SpiderSonArea;
import cn.spider.framework.code.agent.areabase.modules.sonarea.service.ISpiderSonAreaService;
import cn.spider.framework.code.agent.areabase.utils.ExceptionMessage;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import cn.spider.framework.code.agent.function.FunctionManager;
import cn.spider.node.framework.code.agent.sdk.data.*;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
@RestController
@RequestMapping("/areaDomain")
public class AreaDomainInfoController {

    @Autowired
    private AreaDomainInfoService domainInfoService;

    @Resource
    private ISpiderSonAreaService iSpiderSonAreaService;

    @Resource
    private IAreaDomainFunctionInfoService functionInfoService;

    @Resource
    private AreaDatasourceInfoService areaDatasourceInfoService;

    @PostMapping("/areaDomainInit")
    public Wrapper<String> areaInit(@RequestBody @Valid AreaDomainInitParam req) {
        return WrapMapper.ok("初始化成功");
    }

    // 加载所有的领域信息+加载所有的base信息+加载所有的领域插件信息
    @PostMapping("/query_all_area_info")
    public Wrapper<AreaDocInfo> areaDomainInfo() {
        List<AreaDomainInfo> domainInfoList = domainInfoService.lambdaQuery().gt(AreaDomainInfo::getId, 0).list();
        List<SpiderSonArea> spiderSonAreas = iSpiderSonAreaService.lambdaQuery().gt(SpiderSonArea::getId, 0).list();
        List<AreaDomainFunctionInfo> areaDomainFunctionInfos = functionInfoService.lambdaQuery().gt(AreaDomainFunctionInfo::getId, 0).list();
        List<AreaDatasourceInfo> areaDatasourceInfos = areaDatasourceInfoService.lambdaQuery().gt(AreaDatasourceInfo :: getId,0).list();
        // 查询
        AreaDocInfo allAreaInfo = new AreaDocInfo();

        if (CollectionUtils.isNotEmpty(domainInfoList)) {
            List<SonAreaCodeBase> sonAreaCodeBases = JSON.parseArray(JSON.toJSONString(domainInfoList),SonAreaCodeBase.class);
            allAreaInfo.setSonAreaCodeBases(sonAreaCodeBases);
        }

        if(CollectionUtils.isNotEmpty(spiderSonAreas)){
            List<SonAreaInfo> sonAreaInfos = JSON.parseArray(JSON.toJSONString(spiderSonAreas),SonAreaInfo.class);
            allAreaInfo.setSonAreaInfos(sonAreaInfos);
        }

        if(CollectionUtils.isNotEmpty(areaDomainFunctionInfos)){
            List<AreaPluginInfo> areaPluginInfos = JSON.parseArray(JSON.toJSONString(areaDomainFunctionInfos),AreaPluginInfo.class);
            allAreaInfo.setAreaPluginInfos(areaPluginInfos);
        }

        if(CollectionUtils.isNotEmpty(areaDatasourceInfos)){
            List<DatasourceInfo> datasourceInfos = JSON.parseArray(JSON.toJSONString(areaDatasourceInfos),DatasourceInfo.class);
            allAreaInfo.setDatasourceInfos(datasourceInfos);
        }
        return WrapMapper.ok(allAreaInfo);
    }

    // 初始化base


}
