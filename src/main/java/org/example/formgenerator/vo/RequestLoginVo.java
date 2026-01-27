package org.example.formgenerator.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLoginVo {
    @NotBlank(message = "用户账户不能为空。")
    @Size(min=3, max = 16, message = "用户账户必须是3-16个字符。")
    private String userName;
    @NotBlank(message = "用户密码不能为空。")
    @Size(min=3, max = 16, message = "用户密码必须是3-16个字符。")
    private String password;
}
