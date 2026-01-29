package org.example.formgenerator.service;

import freemarker.template.Template;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.common.Constant;
import org.example.formgenerator.dto.TemplateFieldDTO;
import org.example.formgenerator.entity.TemplateField;
import org.example.formgenerator.mapper.TemplateFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class TemplateFieldService {
    @Autowired
    private TemplateFieldMapper templateFieldMapper;
    @Transactional(rollbackFor = Exception.class)
    public Boolean createSingleField(TemplateFieldDTO dto){
        validateFieldParam(dto);
        checkFieldCodeUnique(dto.getFieldTemplateId(), dto.getFieldCode(), null);
        TemplateField field = convertDTOToEntity(dto);
        templateFieldMapper.insert(field);
        log.info("单个创建模板字段成功，字段编码：{}， 模板ID：{}", dto.getFieldCode());
        return true;
    }

    private TemplateField convertDTOToEntity(TemplateFieldDTO dto) {
        TemplateField field = new TemplateField();
        field.setTemplateId(dto.getFieldTemplateId());
        field.setFieldCode(dto.getFieldCode());
        field.setFieldName(dto.getFieldName());
        field.setFieldType(dto.getFieldType());
        field.setFieldOrder(dto.getFieldOrder() == null ? 0:dto.getFieldOrder());
        field.setDefaultValue(dto.getDefaultValue()==null?"":dto.getDefaultValue());
        return field;
    }

    private void validateFieldParam(TemplateFieldDTO dto) {
        if(dto.getFieldTemplateId() == null)        {
            throw  new RuntimeException("模板ID不能为空。");
        }
        if(!StringUtils.hasText(dto.getFieldCode())){
            throw new RuntimeException("字段编码不能为空。");
        }
        if(!StringUtils.hasText(dto.getFieldName())){
            throw new RuntimeException("字段名称不能为空。");
        }
        if(!StringUtils.hasText(dto.getFieldType())){
            throw new RuntimeException("字段类型不能为空。");
        }
    }


    private void checkFieldCodeUnique(Long templateId, String fieldCode, Long excludeId){
        // 1. 入参非空基础校验（快速失败，避免无效SQL）
        Assert.notNull(templateId, "模板ID不能为空");
        Assert.hasText(fieldCode, "字段编码不能为空");
        TemplateField field = templateFieldMapper.selectByTemplateIdAndCode(templateId, fieldCode, excludeId);
        if(field != null)
        {
            throw new RuntimeException("字段编码[" + fieldCode + "]在模板[" + templateId + "]下已存在");
        }
    }
    public List<TemplateField> listFieldsByTemplateId(Long templateId){
        if(templateId == null)
        {
            throw new RuntimeException("模板ID不能为空");
        }
        return templateFieldMapper.selectByTemplateId(templateId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteField(Long id){
        if(id == null){
            throw new RuntimeException("字段ID不能为空。");

        }
        TemplateField deleteField = new TemplateField();
        deleteField.setId(id);
        deleteField.setIsDeleted(Constant.IS_DELETED_YES);
        int deleteCount = templateFieldMapper.updateById(deleteField);

        log.info("删除模板字段{}，结果：{}", id, deleteCount > 0);
        return deleteCount > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateField(TemplateFieldDTO dto){
        if(dto.getId() == null){
            throw new RuntimeException("字段ID不能为空");
        }

        validateFieldParam(dto);

        TemplateField existField = templateFieldMapper.selectById(dto.getId());
        if(existField == null){
            throw new RuntimeException("模板字段不存在，ID："+dto.getId());
        }
        checkFieldCodeUnique(dto.getFieldTemplateId(), dto.getFieldCode(), dto.getId());;
        TemplateField updateField = convertDTOToEntity(dto);
        updateField.setId(dto.getId());
        int  updateCount = templateFieldMapper.updateById(updateField);
        log.info("编辑模板字段{},结果：{}",dto.getId(), updateCount>0);
        return updateCount > 0;
    }
}
