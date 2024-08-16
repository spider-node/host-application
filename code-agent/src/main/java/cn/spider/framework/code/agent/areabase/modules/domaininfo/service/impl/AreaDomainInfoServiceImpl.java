package cn.spider.framework.code.agent.areabase.modules.domaininfo.service.impl;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.QueryDomainParam;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.mapper.AreaDomainInfoMapper;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.service.AreaDomainInfoService;
import cn.spider.framework.code.agent.areabase.modules.sonarea.entity.SpiderSonArea;
import cn.spider.framework.code.agent.areabase.modules.sonarea.service.ISpiderSonAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

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

    @Resource
    private ISpiderSonAreaService spiderSonAreaService;

    @Override
    public AreaDomainInfo queryAreaDomainInfo(QueryDomainParam param) {
        // 查询 子域的信息
        SpiderSonArea sonArea = spiderSonAreaService.lambdaQuery()
                .eq(SpiderSonArea :: getAreaName,param.getAreaName())
                .eq(SpiderSonArea :: getSonAreaName,param.getSonAreaName())
                .one();
        Preconditions.checkArgument(Objects.nonNull(sonArea),"子域的配置不存在,请新增子域");
        AreaDomainInfo areaDomainInfo = super.lambdaQuery()
                .eq(AreaDomainInfo :: getDatasourceName,sonArea.getDatasource())
                .eq(AreaDomainInfo :: getTableName,sonArea.getTableName()).one();
        return areaDomainInfo;
    }

}
