package org.example.formgenerator.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class ImportExcelDTO {

    @NotBlank(message = "数据库名称不能为空")
    private String sourceName;

    @NotNull(message = "数据排列方式不能为空")
    private byte sourceType;

    private String sourceDesc;

    @NotNull(message = "请上传Excel文件")
    private MultipartFile file;

    @NotBlank(message = "创建人不能为空")
    private String creator;
}
