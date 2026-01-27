package org.example.formgenerator.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportTemplateDTO {
    @NotBlank(message = "模板名称不能为空。")
    private String templateName;

    private String templateDesc;

    @NotNull(message = "排列方式不能为空。")
    private byte layoutType;

    @NotBlank(message = "作者不能为空。")
    private String creator;

    @NotBlank(message = "请上传模板文件。")
    private MultipartFile file;
}
