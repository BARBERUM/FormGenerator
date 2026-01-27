package org.example.formgenerator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 模板字段表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("template_field")
public class TemplateField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联模板ID
     */
    private Long templateId;

    /**
     * 字段编码（唯一标识）
     */
    private String fieldCode;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型：text/number/date等
     */
    private String fieldType;

    /**
     * 字段显示顺序
     */
    private Integer fieldOrder;

    /**
     * 字段默认值
     */
    private String defaultValue;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Byte isDeleted;
}
