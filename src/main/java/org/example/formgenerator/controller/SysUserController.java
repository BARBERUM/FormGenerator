package org.example.formgenerator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Delete;
import org.example.formgenerator.entity.SysUser;
import org.example.formgenerator.entity.SysUserRole;
import org.example.formgenerator.service.SysUserService;
import org.example.formgenerator.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/user/{userId}")
    public R<?> getUserById(@PathVariable("userId") Long userId)
    {
        SysUser user = sysUserService.getUserById(userId);
        return R.success("成功查询用户信息。", user);
    }

    @PostMapping("/user")
    public R<?> addUser(@RequestBody SysUser user)
    {
        sysUserService.addSysUser(user);
        return R.success("成功添加用户信息。");
    }

    @PutMapping("/user")
    public R<?> updateUser(@RequestBody SysUser user)
    {
        sysUserService.updateSysUser(user);
        return R.success("成功更新用户信息。");
    }

    @DeleteMapping("/user/{userId}")
    public R<?> deleteUser(@PathVariable ("userId") Long userId)
    {
        sysUserService.deleteSysUser(userId);
        return R.success("成功删除用户信息");
    }


    @GetMapping("/users")
    public R<?> pageUser(@RequestParam(value = "pageNum", required = false, defaultValue = "1")Integer pageNum)
    {
        IPage<SysUser> userIPage = sysUserService.pageUser(pageNum, 5);
        return R.success("用户信息分页列表", userIPage);
    }
}
