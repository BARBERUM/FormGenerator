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
 * 字段映射表（模板字段<->数据源字段）
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("field_mapping")
public class FieldMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 映射ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 数据源ID
     */
    private Long sourceId;

    /**
     * 模板字段编码
     */
    private String templateFieldCode;

    /**
     * 数据源字段编码
     */
    private String sourceFieldCode;

    /**
     * 映射规则（如数据转换公式）
     */
    private String mappingRule;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Byte isDeleted;
}
