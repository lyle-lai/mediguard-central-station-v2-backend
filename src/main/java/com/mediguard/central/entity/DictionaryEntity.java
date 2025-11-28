package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 字典实体类
 */
@Data
@TableName("sys_dictionary")
public class DictionaryEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典分类
     */
    private String category;

    /**
     * 字典代码
     */
    private String code;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 排序
     */
    private Integer sort;
}
