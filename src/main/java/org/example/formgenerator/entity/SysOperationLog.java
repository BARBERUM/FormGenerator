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
 * 系统操作日志表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名（冗余存储，避免用户删除后日志无信息）
     */
    private String username;

    /**
     * 操作类型（如新增模板/导入数据源/生成表单）
     */
    private String operationType;

    /**
     * 操作内容（如“新增模板：员工信息表”）
     */
    private String operationContent;

    /**
     * 关联业务ID（如模板ID/数据源ID）
     */
    private Long businessId;

    /**
     * 业务类型（如template/source/form）
     */
    private String businessType;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    private Byte isDeleted;
}
