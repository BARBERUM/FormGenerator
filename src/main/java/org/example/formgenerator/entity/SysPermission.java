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
 * 系统权限表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限编码（如template:add/template:edit/source:view）
     */
    private String permCode;

    /**
     * 权限名称（如新增模板/编辑模板/查看数据源）
     */
    private String permName;

    /**
     * 父权限ID（用于菜单层级）
     */
    private Long parentId;

    /**
     * 权限类型：1-菜单 2-按钮 3-接口
     */
    private Byte permType;

    /**
     * 排序
     */
    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Byte isDeleted;
}
