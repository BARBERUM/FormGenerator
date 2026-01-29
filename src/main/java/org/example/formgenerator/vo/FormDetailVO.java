package org.example.formgenerator.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.formgenerator.entity.FormData;
import org.example.formgenerator.entity.FormInstance;

import java.util.List;

@Data
public class FormDetailVO {
    @NotNull(message = "表单主信息不能缺失。")
    private FormInstance formInstance;
    @NotNull(message = "表单详细信息不能缺失。")
    private List<FormData> formDataList;
}
