package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AreaDomainResp {
    String domain_object;
    String domain_object_package;
    String domain_object_entity_name;
    String domain_object_service_name;
    String domain_object_service_package;
    String domain_object_service_impl_name;
    String domain_object_service_impl_package;

}
