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
 * 数据源数据表（存储原始行列数据）
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("source_data")
public class SourceData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联数据源ID
     */
    private Long sourceId;

    /**
     * 行索引（标识第几行数据）
     */
    private Integer rowIndex;

    /**
     * 列索引（标识第几列数据）
     */
    private Integer colIndex;

    /**
     * 关联的数据源字段编码
     */
    private String fieldCode;

    /**
     * 具体数据值
     */
    private String dataValue;

    private LocalDateTime createTime;

    private Byte isDeleted;
}
