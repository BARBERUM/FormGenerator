package org.example.formgenerator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormInstanceGenerateDTO {
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @NotNull(message = "数据源ID不能未空")
    private Long sourceId;

    @NotBlank(message = "表单名称不能为空")
    private String formNamePrefix;

    @NotBlank(message = "创建人不能为空")
    private String creator;
}
