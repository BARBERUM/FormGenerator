package org.example.formgenerator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TemplateDTO {
    //模板ID（编辑时传）
    private Long id;

    @NotBlank(message = "模板名称不能为空。")
    private String templateName;

    private String templateDesc;

    @NotNull(message = "模板布局类型不能为空。")
    private Byte layoutType;

    private String creator;
}
