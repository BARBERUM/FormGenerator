package org.example.formgenerator;

import org.example.formgenerator.entity.SysUser;
import org.example.formgenerator.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class FormGeneratorApplicationTests {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Test
    void Test01()
    {
        SysUser adminUser = new SysUser();
         adminUser.setUsername("admin");
         adminUser.setPassword("123");
         adminUser.setRealName("系统管理员");
         adminUser.setEmail("syj20040613@outlook.com");
         adminUser.setPhone("18371985947");
         adminUser.setStatus((byte)1);
         adminUser.setAvatar("");
         adminUser.setIsDeleted((byte)0);
        adminUser.setCreateTime(LocalDateTime.now());
        adminUser.setUpdateTime(LocalDateTime.now());
        sysUserMapper.insert(adminUser);
    }

}
