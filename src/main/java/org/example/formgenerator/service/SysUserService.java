package org.example.formgenerator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.formgenerator.entity.SysUser;
import org.example.formgenerator.entity.SysUserRole;
import org.example.formgenerator.mapper.SysUserMapper;
import org.example.formgenerator.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    public void addSysUser(SysUser user)
    {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.insert(user);
    }

    public void updateSysUser(SysUser user)
    {
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.insert(user);
    }

    public void deleteSysUser(Long userId)
    {
        sysUserMapper.softDeleteUserById(userId);
    }

    public SysUser getUserById(Long userId)
    {
        return sysUserMapper.selectById(userId);
    }

    public IPage<SysUser> pageUser(int pageNum, int pageSize)
    {
        Page<SysUser> p = new Page(pageNum, pageSize);
        IPage<SysUser> pu = sysUserMapper.selectPage(p, null);
        return pu;

    }


    public SysUser login(String userName, String userPassword)throws ApiException
    {

        SysUser sysUser = sysUserMapper.findUserByName(userName);
        if(sysUser == null)
        {
            throw   new ApiException("此用户名不存在。");
        }else if(!sysUser.getPassword().equals(userPassword))
        {
            throw new ApiException("密码错误。");
        }
        return sysUser;

    }
}
