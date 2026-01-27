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
 * 系统用户表
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Getter
@Setter
@ToString
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（登录账号，唯一）
     */
    private String username;

    /**
     * 密码（加密存储，如BCrypt）
     */
    private String password;

    /**
     * 真实姓名（用于操作溯源）
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态：0-禁用 1-正常
     */
    private Byte status;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删 1-已删
     */
    private Byte isDeleted;
}
