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
 * 数据源表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("data_source")
public class DataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据源ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据源名称
     */
    private String sourceName;

    /**
     * 数据排列方式：1-纵向 2-横向
     */
    private Byte sourceType;

    /**
     * 数据源描述
     */
    private String sourceDesc;

    /**
     * 数据条数
     */
    private Integer dataCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String creator;

    private Byte isDeleted;
}
