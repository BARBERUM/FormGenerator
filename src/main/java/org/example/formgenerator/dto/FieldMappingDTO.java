package org.example.formgenerator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FieldMappingDTO {
    private Long id;

    @NotNull(message="模板ID不能为空。")
    private Long templateId;

    @NotNull(message = "数据源ID不能为空。")
    private Long sourceId;

    @NotBlank(message = "模板字段编码不能为空")
    private String templateFieldCode;

    @NotBlank(message = "数据源字段编码不能为空")
    private String sourceFieldCode;

    private String mappingRuld;
}
