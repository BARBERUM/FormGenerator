package org.example.formgenerator.controller;

import jakarta.validation.Valid;
import org.example.formgenerator.service.DataSourceService;
import org.example.formgenerator.utils.R;
import org.example.formgenerator.dto.DataImportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/source")
public class ExcelImportController {
    @Autowired
    private DataSourceService dataSourceService;
    @PostMapping("/import")
    public R<?> importExcel(@Valid DataImportDTO dataImportDTO) throws IOException {
        String result = dataSourceService.importExcelData(dataImportDTO);
        return R.success(result);

    }
}
