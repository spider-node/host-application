package cn.spider.framework.code.agent.areabase.modules.domaininfo.service;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitReq;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
public interface AreaDomainInfoService extends IService<AreaDomainInfo> {

    void areaInit(AreaDomainInitReq req);

}
