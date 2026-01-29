package org.example.formgenerator.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.common.Constant;
import org.example.formgenerator.dto.FieldMappingDTO;
import org.example.formgenerator.entity.DataSource;
import org.example.formgenerator.entity.FieldMapping;
import org.example.formgenerator.entity.FormTemplate;
import org.example.formgenerator.mapper.DataSourceMapper;
import org.example.formgenerator.mapper.FieldMappingMapper;
import org.example.formgenerator.mapper.FormTemplateMapper;
import org.example.formgenerator.utils.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FieldMappingService {
    private final FieldMappingMapper fieldMappingMapper;
    private final FormTemplateMapper formTemplateMapper;
    private final DataSourceMapper dataSourceMapper;

    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMapping(FieldMappingDTO dto)
    {
        //基础校验：模板数据源是否有效
        checkTemplateAndSourceExist(dto.getTemplateId(), dto.getSourceId());
        if(fieldMappingMapper.countUniqueMapping(dto.getTemplateId(),dto.getSourceId(),dto.getTemplateFieldCode(), dto.getId())>0){
            throw new ApiException("该模板字段已经存在于当前关系配置中，请勿重新添加。");
        }

        FieldMapping fieldMapping = new FieldMapping();
        BeanUtils.copyProperties(dto, fieldMapping);
        if(dto.getId() == null){
            fieldMappingMapper.insert(fieldMapping);
            log.info("新增字段映射关系成功，模板ID：{}，数据源ID：{}，模板字段：{}",
                    dto.getTemplateId(), dto.getSourceId(), dto.getTemplateFieldCode());

        }else{
            fieldMappingMapper.updateById(fieldMapping);
            log.info("编辑字段映射关系成功。映射ID：{}", dto.getId());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMapping(Long id)
    {
        FieldMapping mapping = fieldMappingMapper.selectById(id);
        if(mapping == null||mapping.getIsDeleted() == 1)
        {
            throw new ApiException("映射关系不存在或者已经删除");
        }
        fieldMappingMapper.deleteById(mapping);
        log.info("字段映射关系删除成功，映射ID：{}",id);
        return true;
    }

    private void checkTemplateAndSourceExist(@NotNull(message="模板ID不能为空。") Long templatedId, @NotNull(message = "数据源ID不能为空。") Long sourceId) {
        FormTemplate template = formTemplateMapper.selectById(templatedId);
        if(template == null || template.getIsDeleted() == Constant.IS_DELETED_YES){
            throw new ApiException("模板不存在或者已经删除。");
        }
        DataSource dataSource = dataSourceMapper.selectById(sourceId);
        if(dataSource == null || dataSource.getIsDeleted() == Constant.IS_DELETED_YES){
            throw new ApiException("数据表不存在或者已经删除。");

        }
    }

    public List<FieldMapping> listMapping(Long templateId, Long sourceId)
    {
        checkTemplateAndSourceExist(templateId, sourceId);
        return fieldMappingMapper.selectByTemplateAndSource(templateId, sourceId);
    }
}
