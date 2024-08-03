package cn.spider.framework.code.agent.areabase.modules.domaininfo.service.impl;

import cn.hutool.core.lang.Assert;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.AreaDatasourceInfoService;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitReq;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.mapper.AreaDomainInfoMapper;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.service.AreaDomainInfoService;
import cn.spider.framework.code.agent.areabase.utils.CodeGenerator3;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
@Service
public class AreaDomainInfoServiceImpl extends ServiceImpl<AreaDomainInfoMapper, AreaDomainInfo> implements AreaDomainInfoService {

    @Autowired
    private AreaDatasourceInfoService datasourceInfoService;

    @Override
    public void areaInit(AreaDomainInitReq req) {
        AreaDatasourceInfo areaDatasourceInfo = datasourceInfoService.getBaseMapper().selectById(req.getDatasourceId());
        Assert.notNull(areaDatasourceInfo,"当前数据源信息不存在，请核实后在请求");
        // 生成代码
        CodeGenerator3.initCode(areaDatasourceInfo,req);
        // 生成pom文件
        CodeGenerator3.initPom(req);
        // 组装areaDomainInfo
        AreaDomainInfo areaDomainInfo = CodeGenerator3.initAreaDomainInfo(areaDatasourceInfo, req);
        AreaDomainInfo exitAreaDomainInfo = super.lambdaQuery().eq(AreaDomainInfo::getTableName,req.getTableName()).eq(AreaDomainInfo::getDatasourceId,areaDatasourceInfo.getId()).one();
        // 如果存在就做修改否则新增
        if (exitAreaDomainInfo!=null) areaDomainInfo.setId(exitAreaDomainInfo.getId());
        super.saveOrUpdate(areaDomainInfo);
    }
}
