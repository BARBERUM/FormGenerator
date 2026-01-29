package org.example.formgenerator.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.common.Constant;
import org.example.formgenerator.dto.FieldMappingDTO;
import org.example.formgenerator.dto.FormInstanceGenerateDTO;
import org.example.formgenerator.entity.*;
import org.example.formgenerator.mapper.*;
import org.example.formgenerator.utils.ApiException;
import org.example.formgenerator.utils.SnowFlakeUtil;
import org.example.formgenerator.vo.SourceDataVO;
import org.example.formgenerator.vo.SourceRowDataVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormInstanceService {
    private final FormInstanceMapper formInstanceMapper;
    private final FormDataMapper formDataMapper;
    private final FieldMappingMapper fieldMappingMapper;
    private final FormTemplateMapper formTemplateMapper;
    private final DataSourceMapper dataSourceMapper;
    private final SourceDataMapper sourceDataMapper;


    // 雪花算法实例（全局唯一，一次初始化）
    private final SnowFlakeUtil snowFlakeUtil = SnowFlakeUtil.getInstance();

    @Transactional(rollbackFor = Exception.class)
    public List<Long> generatorFormInstance(FormInstanceGenerateDTO dto){
        //基础校验
        FormTemplate template = checkTemplateAndSource(dto);
        List<FieldMapping> mappingsList = fieldMappingMapper.selectByTemplateAndSource(dto.getTemplateId(),dto.getSourceId());
        if(CollectionUtils.isEmpty(mappingsList)){
            throw  new ApiException("该模板域数据源未配置字段映射关系，无法生成表单");
        }
        //获取数据
        Map<Integer,SourceRowDataVO> sourceRowDataMap = bulidSourceDataMap(dto.getSourceId());
        if(CollectionUtils.isEmpty(sourceRowDataMap))
        {
            throw new ApiException("该数据源下无有效原始数据，无法生成表单。");
        }
        //遍历映射关系，根据映射关系填写实例表格。
        List<Long> formIdList = new ArrayList<>();
        for(Map.Entry<Integer, SourceRowDataVO> entry : sourceRowDataMap.entrySet()){
            Integer rowIndex = entry.getKey();
            SourceRowDataVO rowDataVO = entry.getValue();

//            //导入逻辑行号做生成表名
//            FormInstance formInstance = buildFormInstance(dto, template, rowIndex);
//            formInstanceMapper.insert(formInstance);

/*            FormInstance formInstance;
            synchronized (formNoLock) { // 本地锁：单服务内串行，避免循环内重复查询
                // 1. 加锁查询最新最大编号（数据库行锁+本地锁，双重保障原子性）
                String newFormNo = formInstanceMapper.getMaxFormNoWithLock();
                log.info("NewFormNo:{}", newFormNo);
                // 2. 构建表单实例（复用你的buildFormInstance，仅新增传递formNo参数）
                formInstance = buildFormInstance(dto, template, rowIndex, newFormNo);
                // 3. 插入表单实例（保留原有插入逻辑）
                formInstanceMapper.insert(formInstance);
            }*/

            // ========== 2. 核心修改：雪花算法生成唯一表单编号（无需查询、无需加锁） ==========
            String uniqueFormNo = snowFlakeUtil.generateUniqueNo("FORM_");
            // 构建表单实例（复用原有方法，直接传雪花算法生成的编号）
            FormInstance formInstance = buildFormInstance(dto, template, rowIndex, uniqueFormNo);
            // 插入表单实例（原有逻辑，无改动）
            formInstanceMapper.insert(formInstance);


            Long formId = formInstance.getId();
            if(formId == null){
                throw new ApiException("第" + rowIndex +"行数据生成表单实例失败，未获取到表单ID");

            }

            List<FormData>formDataList = buildFormDataList(formId, mappingsList, rowDataVO.getFieldDataMap());
            if(!CollectionUtils.isEmpty(formDataList)){
                formDataMapper.batchInsertFormData(formDataList);
            }
            formIdList.add(formId);
            log.info("第{}行数据生成表单成功，表单ID：{}， 表单编号：{}",rowIndex, formId, formInstance.getFormNo());
        }
        log.info("表单实例批量生成成功，共处理{}行原始数据，生成{}个表单实例。",sourceRowDataMap.size(), formIdList.size());
        return formIdList;
    }

    private List<FormData> buildFormDataList(Long formId, List<FieldMapping> mappingsList, Map<String, SourceDataVO> fieldDataMap) {
        List<FormData> formDataList = new ArrayList<>();
        for(FieldMapping mapping:mappingsList){
            FormData formData = new FormData();
            formData.setFormId(formId);
            formData.setFieldCode(mapping.getTemplateFieldCode());

            SourceDataVO sourceDataVO = fieldDataMap.get(mapping.getSourceFieldCode());
            if(sourceDataVO != null)
            {
                formData.setFieldValue(sourceDataVO.getDataValue());
                formData.setSourceDataId(sourceDataVO.getId());
            }else{
                formData.setFieldValue("");
            }
            formDataList.add(formData);

        }
        return formDataList;
    }

    // 添加表单实例对象。
    private FormInstance buildFormInstance(FormInstanceGenerateDTO dto, FormTemplate template, Integer rowIndex, String formNo) {
        FormInstance formInstance = new FormInstance();
        formInstance.setFormNo(formNo);
        formInstance.setTemplateId(dto.getTemplateId());
        formInstance.setSourceId(dto.getSourceId());
        formInstance.setFormName(dto.getFormNamePrefix()+"_第" + rowIndex+"行");
        formInstance.setLayoutType(template.getLayoutType());
        formInstance.setCreator(dto.getCreator());

        return formInstance;
    }

    private FormTemplate checkTemplateAndSource(FormInstanceGenerateDTO dto) {
        FormTemplate template = formTemplateMapper.selectById(dto.getTemplateId());
        if(template == null||template.getIsDeleted().equals(Constant.IS_DELETED_YES))
        {
            throw new ApiException("模板不存在或者已删除。");
        }
        DataSource source = dataSourceMapper.selectById(dto.getSourceId());
        if(source == null||template.getIsDeleted().equals(Constant.IS_DELETED_YES))
        {
            throw new ApiException("数据源不存在或者已删除。");
        }
        return template;
    }

    private Map<Integer, SourceRowDataVO> bulidSourceDataMap(@NotNull(message = "数据源ID不能未空") Long sourceId) {
        List<SourceDataVO> sourceDataList = formDataMapper.selectSourceDataByCode(sourceId);
        if(CollectionUtils.isEmpty(sourceDataList)){
            return new HashMap<>();
        }

        Map<Integer, SourceRowDataVO>sourceRowDataVOMap = new HashMap<>();
        for(SourceDataVO vo:sourceDataList){
            Integer rowIndex = getSourceDataRowIndex(vo.getId());
            SourceRowDataVO rowDataVO = sourceRowDataVOMap.getOrDefault(rowIndex, new SourceRowDataVO());
            rowDataVO.setRowIndex(rowIndex);

            if(rowDataVO.getFieldDataMap() == null){
                rowDataVO.setFieldDataMap(new HashMap<>());
            }

            rowDataVO.getFieldDataMap().put(vo.getFieldCode(),vo);

            sourceRowDataVOMap.put(rowIndex,rowDataVO);
        }
        return sourceRowDataVOMap;
    }

    private Integer getSourceDataRowIndex(Long id) {
        return sourceDataMapper.getLogicalRowIndexBySourceDataId(id);
    }

    public FormInstance getById(Long formId) {
        return formInstanceMapper.selectById(formId);
    }

    public List<FormData> getFormDataByFormId(Long formId)
    {
        return formDataMapper.selectFormDataByFormId(formId);
    }

    public Long softDeleteFormByFormId(Long formId)
    {
        if(formId == null)
        {
            throw new ApiException("id为"+formId+"的实例表格不存在。");
        }
        formInstanceMapper.softDeleteFromByFormId(formId);
        List<FormData> formDataList = formDataMapper.selectFormDataByFormId(formId);
        for(FormData formData : formDataList)
        {
            formDataMapper.softDeleteFromDataByFormId(formId);
        }
        return formId;
    }
}
