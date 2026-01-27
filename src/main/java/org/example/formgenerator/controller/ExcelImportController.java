package org.example.formgenerator.controller;

import jakarta.validation.Valid;
import org.example.formgenerator.service.DataSourceService;
import org.example.formgenerator.utils.R;
import org.example.formgenerator.vo.ImportExcelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/source")
public class ExcelImportController {
    @Autowired
    private DataSourceService dataSourceService;
    @PostMapping("/import")
    public R<?> importExcel(@Valid ImportExcelDTO importExcelDTO) throws IOException {
        String result = dataSourceService.importExcelData(importExcelDTO);
        return R.success(result);

    }
}
