package com.mall.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类基类
 *
 * <p>所有数据库实体类的父类，定义了公共字段：</p>
 * <ul>
 *   <li>id - 主键，自增</li>
 *   <li>createTime - 创建时间，插入时自动填充</li>
 *   <li>updateTime - 更新时间，插入或更新时自动填充</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public abstract class BaseEntity implements Serializable {

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 创建时间，插入时自动填充 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间，插入或更新时自动填充 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
