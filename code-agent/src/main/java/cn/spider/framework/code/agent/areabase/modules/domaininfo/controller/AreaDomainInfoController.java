package cn.spider.framework.code.agent.areabase.modules.domaininfo.controller;

import cn.hutool.json.JSONUtil;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainReq;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainResp;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.service.AreaDomainInfoService;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
@RestController
@RequestMapping("/areaDomain")
public class AreaDomainInfoController {
    @Autowired
    private AreaDomainInfoService areaDomainInfoService;
    @Autowired
    private AreaDomainInfoService domainInfoService;

    @PostMapping("/areaDomainInit")
    public Wrapper<String> areaInit(@RequestBody @Valid AreaDomainInitParam req) {
        areaDomainInfoService.areaInit(req);
        return WrapMapper.ok("初始化成功");
    }

    @PostMapping("/areaDomainInfo")
    public Wrapper<String> areaDomainInfo(@RequestBody @Valid AreaDomainReq req) {
        AreaDomainInfo exitAreaDomainInfo = domainInfoService.lambdaQuery().eq(AreaDomainInfo::getTableName,req.getTableName()).eq(AreaDomainInfo::getDatasourceId,req.getDatasourceId()).one();
        AreaDomainResp areaDomainResp = new AreaDomainResp();
        areaDomainResp.setDomain_object(exitAreaDomainInfo.getDomainObject());
        areaDomainResp.setDomain_object_package(exitAreaDomainInfo.getDomainObjectPackage());
        areaDomainResp.setDomain_object_entity_name(exitAreaDomainInfo.getDomainObjectEntityName());
        areaDomainResp.setDomain_object_service_name(exitAreaDomainInfo.getDomainObjectServiceName());
        areaDomainResp.setDomain_object_service_package(exitAreaDomainInfo.getDomainObjectServicePackage());
        areaDomainResp.setDomain_object_service_impl_name(exitAreaDomainInfo.getDomainObjectServiceImplName());
        areaDomainResp.setDomain_object_service_impl_package(exitAreaDomainInfo.getDomainObjectServiceImplPackage());
        return WrapMapper.ok(JSONUtil.toJsonStr(areaDomainResp));
    }


}
