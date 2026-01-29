package org.example.formgenerator.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.core.util.CollectionUtil;
import org.example.formgenerator.common.Constant;
import org.example.formgenerator.dto.TemplateDTO;
import org.example.formgenerator.dto.TemplateImportDTO;
import org.example.formgenerator.entity.FormTemplate;
import org.example.formgenerator.entity.TemplateField;
import org.example.formgenerator.listener.ExcelTemplateListener;
import org.example.formgenerator.mapper.FormTemplateMapper;
import org.example.formgenerator.mapper.TemplateFieldMapper;
import org.example.formgenerator.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormTemplateService {
    @Autowired
    FormTemplateMapper formTemplateMapper;
    private final TemplateFieldMapper templateFieldMapper;
    private final TemplateFieldService templateFieldService;


    @Transactional(rollbackFor = Exception.class)
    public Long createTemplate(TemplateDTO dto)
    {
        if(!StringUtils.hasText(dto.getTemplateName())){
            throw new RuntimeException("模板名称不能为空。");

        }
        if(dto.getLayoutType() == null){
            throw new RuntimeException("模板布局类型不能为空");
        }

        FormTemplate template = new FormTemplate();
        template.setTemplateName(dto.getTemplateName());
        template.setTemplateDesc(dto.getTemplateDesc());
        template.setLayoutType(dto.getLayoutType());
        template.setCreator(dto.getCreator());

        formTemplateMapper.insert(template);
        log.info("手动创建模板主表成功，模板ID：{}", template.getId());
        return template.getId();

    }


    @Transactional(rollbackFor = Exception.class)
    public Long importTemplate(TemplateImportDTO importDTO){
        try{
            String originalFilename = importDTO.getImportFile().getOriginalFilename();
            if(!StringUtils.hasText(originalFilename)||(!originalFilename.endsWith(".xlsx"))&&!originalFilename.endsWith(".xls")){
                throw new ApiException("请上传Excel格式文件（.xlsx/.xls）");
            }
            FormTemplate template = new FormTemplate();
            template.setTemplateName(importDTO.getImportTemplateName());
            template.setTemplateDesc(importDTO.getImportTemplateDesc());
            template.setLayoutType(importDTO.getImportLayoutType());
            template.setCreator(importDTO.getImportCreator());
            formTemplateMapper.insert(template);
            ExcelTemplateListener listener = new ExcelTemplateListener(template.getId(), importDTO.getImportLayoutType());
            EasyExcel.read(importDTO.getImportFile().getInputStream(), listener)
                    .headRowNumber(0)
                    .sheet()
                    .doRead();
            List<TemplateField> fieldList = listener.getTemplateFiledList();
            templateFieldMapper.batchInsert(fieldList);

            log.info("Excel导入模板完成，模板ID：{}，字段数：{}", template.getId(), fieldList.size());
            return template.getId();
        } catch (IOException e) {
            throw new RuntimeException("Excel模板中未解析到有效字段，请检查表头。");
        }catch (Exception e){
            log.error("模板写入失败。");
            throw new RuntimeException("模板导入失败:"+ e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTemplate(TemplateDTO dto){
        if(dto.getId() == null)
        {
            throw new RuntimeException("模板字段不能为空");
        }

        FormTemplate existTemplate = formTemplateMapper.selectById(dto.getId());
        if(existTemplate == null)
        {
            throw new RuntimeException("模板不存在，ID："+dto.getId());
        }

        FormTemplate updateTemplate = new FormTemplate();
        updateTemplate.setId(dto.getId());
        updateTemplate.setTemplateDesc(dto.getTemplateDesc());
        updateTemplate.setTemplateName(dto.getTemplateName());
        updateTemplate.setLayoutType(dto.getLayoutType());
        int updateCount = formTemplateMapper.updateById(updateTemplate);
        log.info("编辑模板主表{}，结果：{}", dto.getId(), updateCount>0);
        return updateCount > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTemplate(Long id){
        if(id == null){
            throw new RuntimeException("模板ID不能为空");
        }

        FormTemplate deleteTemplate = new FormTemplate();
        deleteTemplate.setId(id);
        deleteTemplate.setIsDeleted(Constant.IS_DELETED_YES);
        formTemplateMapper.updateById(deleteTemplate);

        List<TemplateField> fieldList = templateFieldService.listFieldsByTemplateId(id);
        if(!CollectionUtils.isEmpty(fieldList)){
            fieldList.forEach(field->templateFieldService.deleteField(field.getId()));
        }
        log.info("删除模板{}（含{}个字段）", id, fieldList.size());
        return true;
    }

    public List<FormTemplate> listTemplates(String templateName){
        LambdaQueryWrapper<FormTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FormTemplate::getIsDeleted, Constant.IS_DELETED_NO)
                .orderByDesc(FormTemplate::getCreateTime);
        if (StringUtils.hasText(templateName)) {
            wrapper.like(FormTemplate::getTemplateName, templateName);
        }
        return formTemplateMapper.selectList(wrapper);

    }


    public FormTemplate getTemplateDetail(Long id){
        if(id == null){

            throw new RuntimeException("模板ID不能为空。");

        }
        FormTemplate template = formTemplateMapper.selectById(id);
        if(template == null){
            throw new RuntimeException("模板不存在，ID：" + id);
        }
        return template;
    }
}
