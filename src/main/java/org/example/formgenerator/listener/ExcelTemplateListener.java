package org.example.formgenerator.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.example.formgenerator.common.Constant;
import org.example.formgenerator.entity.TemplateField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelTemplateListener extends AnalysisEventListener<Map<Integer, String>> {
    private final Byte layoutType;
    private final Long templateId;
    private final List<TemplateField> templateFiledList = new ArrayList<>();
    private final List<Map<Integer,String>>allRowData = new ArrayList<>();

    public ExcelTemplateListener(Long templateId, Byte layoutType){
        this.templateId = templateId;
        this.layoutType = layoutType;
    }

    @Override
    public void invoke(Map<Integer, String> dataMap, AnalysisContext context) {
        log.info("解析到模板数据行:{}", dataMap);
        if(Constant.LAYOUT_TYPE_VERTICAL.equals(layoutType))
        {
            parseVerticalTemplateField(dataMap);
        }else if(Constant.LAYOUT_TYPE_HORIZONTAL.equals(layoutType))
        {
            allRowData.add(dataMap);
        }
    }



    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(Constant.LAYOUT_TYPE_HORIZONTAL.equals(layoutType))
        {
            parseHorizontalTemplateField();
        }
        log.info("模板字段解析完成，共解析了{}个有效字段。",templateFiledList.size());
    }

    private void parseHorizontalTemplateField() {
        for(int rowIndex = 0; rowIndex<allRowData.size(); ++rowIndex)
        {
            Map<Integer, String> rowData = allRowData.get(rowIndex);
            String fieldName = rowData.get(0) == null?"":rowData.get(0).trim();
            if(fieldName.isEmpty())
            {
                log.warn("横向模板解析：行索引{}的表头为空，跳过", rowIndex);
                continue;
            }
            TemplateField templateField = new TemplateField();
            templateField.setTemplateId(templateId);
            templateField.setFieldCode("field_" + rowIndex);
            templateField.setFieldName(fieldName);
            templateField.setFieldType(Constant.FIELD_TYPE_TEXT);
            templateField.setFieldOrder(rowIndex);
            templateField.setDefaultValue("");
            templateFiledList.add(templateField);
        }
    }

    private void parseVerticalTemplateField(Map<Integer, String> headMap) {
        for(Map.Entry<Integer, String> entry:headMap.entrySet())
        {
            Integer colIndex = entry.getKey();
            String fieldName = entry.getValue() == null?"":entry.getValue();
            if(fieldName.isEmpty()){
                log.warn("纵向模板解析：列索引{}的表头为空，跳过。",colIndex);
                continue;
            }
            TemplateField field = new TemplateField();
            field.setFieldCode("field_"+colIndex);
            field.setTemplateId(templateId);
            field.setFieldName(fieldName);
            field.setFieldType(Constant.FIELD_TYPE_TEXT);
            field.setFieldOrder(colIndex);
            field.setDefaultValue("");
            templateFiledList.add(field);

        }
    }

    public List<TemplateField> getTemplateFiledList() {
        return templateFiledList;
    }
}
