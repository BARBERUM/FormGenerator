package org.example.formgenerator.controller;


import jakarta.validation.Valid;
import org.example.formgenerator.entity.SysUser;
import org.example.formgenerator.service.SysUserService;
import org.example.formgenerator.utils.R;
import org.example.formgenerator.vo.RequestLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private final SysUserService sysUserService;

    public LoginController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/login")
    public R<?> login(@Valid  @RequestBody RequestLoginVo requestLoginVo)
    {
        SysUser sysUser = sysUserService.login(requestLoginVo.getUserName()
                , requestLoginVo.getPassword());
        return R.success("登录成功",sysUser);
    }
}
