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
 * 表单实例表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("form_instance")
public class FormInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表单ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表单编号（唯一）
     */
    private String formNo;

    /**
     * 关联模板ID
     */
    private Long templateId;

    /**
     * 关联数据源ID
     */
    private Long sourceId;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单布局类型：1-纵向 2-横向
     */
    private Byte layoutType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String creator;

    private Byte isDeleted;
}
