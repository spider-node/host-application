package cn.spider.framework.code.agent.areabase.modules.sonarea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 子域
 * </p>
 *
 * @author dds
 * @since 2024-08-14
 */
@Data
@TableName("spider_son_area")
public class SpiderSonArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 领域id
     */
    private String areaId;

    /**
     * 主域名称
     */
    private String areaName;

    /**
     * 领域名称
     */
    private String sonAreaName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 数据源名称
     */
    private String datasource;

    /**
     * 创建事件
     */
    private LocalDateTime createTime;
}
