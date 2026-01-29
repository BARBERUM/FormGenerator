package org.example.formgenerator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TemplateFieldDTO {
    private Long id;

   @NotNull(message = "模板ID不能为空。")
    private Long fieldTemplateId;

   @NotBlank(message = "字段编码不能为空。")
    private String fieldCode;

   @NotBlank(message = "字段名称不能为空。")
    private String fieldName;

   @NotBlank(message = "字段类型不能为空。")
    private String fieldType;

   private Integer fieldOrder;

   private String defaultValue;



}
