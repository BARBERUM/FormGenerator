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
 * 表单数据明细表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("form_data")
public class FormData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联表单ID
     */
    private Long formId;

    /**
     * 模板字段编码
     */
    private String fieldCode;

    /**
     * 填充的字段值
     */
    private String fieldValue;

    /**
     * 溯源：关联的数据源数据ID
     */
    private Long sourceDataId;

    private LocalDateTime createTime;

    private Byte isDeleted;
}
