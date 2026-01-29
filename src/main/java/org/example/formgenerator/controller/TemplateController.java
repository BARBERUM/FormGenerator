package org.example.formgenerator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.dto.TemplateDTO;
import org.example.formgenerator.dto.TemplateFieldDTO;
import org.example.formgenerator.dto.TemplateImportDTO;
import org.example.formgenerator.entity.FormTemplate;
import org.example.formgenerator.entity.TemplateField;
import org.example.formgenerator.service.FormTemplateService;
import org.example.formgenerator.service.TemplateFieldService;
import org.example.formgenerator.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/template")
@RequiredArgsConstructor
public class TemplateController {
    private final FormTemplateService formTemplateService;
    private final TemplateFieldService templateFieldService;

    @PostMapping("/create")
    public R<?> creaTemplate(@Valid TemplateDTO dto){
        Long templateId = formTemplateService.createTemplate(dto);
        return  R.success("模板创建成功", templateId);
    }

    @PostMapping("/import")
    public R<?> importTemplate(@Valid TemplateImportDTO importDTO){
        log.info("templateName:{}",importDTO.getImportTemplateName());
        Long templateId = formTemplateService.importTemplate(importDTO);
        return R.success("模板创建成功", 0);
    }

    @PutMapping("/update")
    public R<?> updateTemplate(@Valid TemplateDTO dto){
            Boolean result = formTemplateService.updateTemplate(dto);
            return R.success("模板编辑成功。",result);
    }

    @GetMapping("/list")
    public R<?> listTemplates(@RequestParam(required = false) String templateName){
        List<FormTemplate> list = formTemplateService.listTemplates(templateName);
        return R.success("模板列表查询成功。",list);
    }

    @DeleteMapping("/delete/{id}")
    public R<?> deleteTemplate(@PathVariable Long id){
        Boolean result = formTemplateService.deleteTemplate(id);
        return R.success("模板删除成功。", result);
    }

    @PostMapping("/field/create")
    public R<?> createSingleField(@Valid @RequestBody TemplateFieldDTO dto){
        Boolean result = templateFieldService.createSingleField(dto);
        return R.success("单个字段创建成功。", result);
    }

    @PostMapping("/field/update")
    public R<?> updateField(@Valid @RequestBody TemplateFieldDTO dto){
        Boolean result = templateFieldService.updateField(dto);
        return R.success("字段编辑成功",result);
    }

    @DeleteMapping("/field/delete/{id}")
    public R<?> deleteField(@PathVariable Long id){
        Boolean result = templateFieldService.deleteField(id);
        return R.success("字段删除成功。",result);
    }

    @GetMapping("/field/list/{templateId}")
    public R<?> updateField(@PathVariable Long templateId){
        List<TemplateField> list = templateFieldService.listFieldsByTemplateId(templateId);
        return R.success("字段列表查询成功", list);
    }
}
