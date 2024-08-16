package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AreaDomainResp {
    private String domain_object;
    private String domain_object_package;
    private String domain_object_entity_name;
    private  String domain_object_service_name;
    private String domain_object_service_package;
    private  String domain_object_service_impl_name;
    private String domain_object_service_impl_package;
    private String group_Id;
    private String table_Name;
    private String datasource;
}
