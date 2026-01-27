package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.formgenerator.entity.SysUser;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Update("update sys_user set is_deleted = 1 where id = #{userId}")
    public void softDeleteUserById(@Param("userId") Long userId);

    @Select("Select * from sys_user where username = #{userName}")
    public SysUser findUserByName(@Param("userName") String userName);

}
