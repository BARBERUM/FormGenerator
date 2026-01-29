package org.example.formgenerator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TemplateFieldCreateDTO {
    @NotNull(message = "模板ID不能为空。")
    private Long templateId;

    @NotNull(message = "模板字段列表不能为空")
    private List<TemplateFieldDTO> fieldList;
}
