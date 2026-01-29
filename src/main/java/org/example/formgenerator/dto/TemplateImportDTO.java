package org.example.formgenerator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TemplateImportDTO {
    @NotBlank(message = "模板名称不能为空。")
    private String importTemplateName;

    private String importTemplateDesc;

    @NotNull(message = "排列方式不能为空。")
    private byte importLayoutType;

    @NotBlank(message = "创建人不能为空。")
    private String importCreator;
    //如果已有模板文件就传入已有的模板文件，如果没有则可以进行新建。但是当前请求是以存在模板文件为前提。
    @NotNull(message = "请上传模板文件。")
    private MultipartFile importFile;
}
