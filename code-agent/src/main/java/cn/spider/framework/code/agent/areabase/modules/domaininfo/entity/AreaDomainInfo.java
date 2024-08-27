package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ATu
 * @since 2024-07-25
 */
@Getter
@Setter
@TableName("area_domain_base_info")
public class AreaDomainInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("datasource_id")
    private Integer datasourceId;

    @TableField("datasource_name")
    private String datasourceName;

    @TableField("table_name")
    private String tableName;

    /**
     * 子域名称
     */
    @TableField("son_area_name")
    private String sonAreaName;

    @TableField("domain_object")
    private String domainObject;

    @TableField("domain_object_package")
    private String domainObjectPackage;

    @TableField("domain_object_entity_name")
    private String domainObjectEntityName;

    @TableField("domain_object_service_name")
    private String domainObjectServiceName;

    @TableField("domain_object_service_package")
    private String domainObjectServicePackage;

    @TableField("domain_object_service_impl_name")
    private String domainObjectServiceImplName;

    @TableField("domain_object_service_impl_package")
    private String domainObjectServiceImplPackage;

    @TableField("version")
    private String version;

    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * pom中的groupId
     */
    @TableField("group_id")
    private String groupId;

    /**
     * pom中的artifactId
     */
    @TableField("artifact_id")
    private String artifactId;

    /**
     * 领域id
     */
    @TableField("area_id")
    private String areaId;

    /**
     * 领域id
     */
    @TableField("area_name")
    private String areaName;
}
