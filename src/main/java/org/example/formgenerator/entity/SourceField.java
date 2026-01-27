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
 * 数据源字段表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("source_field")
public class SourceField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联数据源ID
     */
    private Long sourceId;

    /**
     * 字段编码
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
     * 字段顺序
     */
    private Integer fieldOrder;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Byte isDeleted;
}
