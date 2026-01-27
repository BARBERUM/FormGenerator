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
 * 系统角色表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色编码（如admin/user/read）
     */
    private String roleCode;

    /**
     * 角色名称（如超级管理员/普通用户/只读用户）
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Byte isDeleted;
}
