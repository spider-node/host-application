package cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
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
@Data
@TableName("area_datasource_info")
public class AreaDatasourceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("url")
    private String url;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("create_time")
    private LocalDateTime createTime;
}
