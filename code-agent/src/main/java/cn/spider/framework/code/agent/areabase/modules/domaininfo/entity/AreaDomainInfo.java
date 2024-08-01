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
@TableName("area_domain_info")
public class AreaDomainInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("datasource_id")
    private Integer datasourceId;

    @TableField("table_name")
    private String tableName;

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

    @TableField("domain_object_business_class_package")
    private String domainObjectBusinessClassPackage;

    @TableField("domain_object_business_method_param_package")
    private String domainObjectBusinessMethodParamPackage;

    @TableField("create_time")
    private LocalDateTime createTime;
}
