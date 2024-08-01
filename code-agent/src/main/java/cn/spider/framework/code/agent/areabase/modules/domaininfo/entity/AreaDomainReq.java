package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AreaDomainReq {
    @NotBlank(message = "tableName不能为空")
    String tableName;
    @NotNull(message = "datasourceId不能为空")
    Integer datasourceId;

}
