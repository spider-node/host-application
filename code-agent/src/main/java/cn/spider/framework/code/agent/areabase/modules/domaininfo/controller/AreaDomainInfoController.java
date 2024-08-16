package cn.spider.framework.code.agent.areabase.modules.domaininfo.controller;

import cn.hutool.json.JSONUtil;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.*;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.service.AreaDomainInfoService;
import cn.spider.framework.code.agent.areabase.utils.ExceptionMessage;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import cn.spider.framework.code.agent.function.FunctionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private FunctionManager functionManager;

    @PostMapping("/areaDomainInit")
    public Wrapper<String> areaInit(@RequestBody @Valid AreaDomainInitParam req) {
        return WrapMapper.ok("初始化成功");
    }

    @PostMapping("/query_son_area_info")
    public Wrapper<AreaDomainResp> areaDomainInfo(@RequestBody @Valid QueryDomainParam param) {
        try {
            AreaDomainInfo areaDomainInfo = domainInfoService.queryAreaDomainInfo(param);
            if (Objects.isNull(areaDomainInfo)) {
                AreaDomainInitParam initParam = new AreaDomainInitParam();
                initParam.setAreaName(param.getAreaName());
                initParam.setSonAreaName(param.getSonAreaName());
                functionManager.initBaseProject(initParam);
                areaDomainInfo = domainInfoService.queryAreaDomainInfo(param);
            }
            AreaDomainResp areaDomainResp = new AreaDomainResp();
            areaDomainResp.setDomain_object(areaDomainInfo.getDomainObject());
            areaDomainResp.setDomain_object_package(areaDomainInfo.getDomainObjectPackage());
            areaDomainResp.setDomain_object_entity_name(areaDomainInfo.getDomainObjectEntityName());
            areaDomainResp.setDomain_object_service_name(areaDomainInfo.getDomainObjectServiceName());
            areaDomainResp.setDomain_object_service_package(areaDomainInfo.getDomainObjectServicePackage());
            areaDomainResp.setDomain_object_service_impl_name(areaDomainInfo.getDomainObjectServiceImplName());
            areaDomainResp.setDomain_object_service_impl_package(areaDomainInfo.getDomainObjectServiceImplPackage());
            areaDomainResp.setGroup_Id(areaDomainInfo.getGroupId());
            areaDomainResp.setTable_Name(areaDomainInfo.getTableName());
            areaDomainResp.setDatasource(areaDomainInfo.getDatasourceName());
            return WrapMapper.ok(areaDomainResp);
        } catch (Exception e) {
            return WrapMapper.error(ExceptionMessage.getStackTrace(e));
        }
    }


}
