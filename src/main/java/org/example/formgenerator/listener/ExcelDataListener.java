package org.example.formgenerator.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mysql.cj.jdbc.CallableStatementWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.entity.SourceData;
import org.example.formgenerator.entity.SourceField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelDataListener extends AnalysisEventListener<Map<Integer,String>> {
    //存储表头
    private List<SourceField> sourceFieldList = new ArrayList<>();
    //存储表格内容
    private List<SourceData>sourceDataList = new ArrayList<>();
    //数据源ID（关联data_source表格）
    private Long sourceId;
    //数据排列方式
    private byte sourceType;

    //临时存储所有行数据（横向排列时需要收集所有的行，再按列解析）
    private List<Map<Integer,String>> allRowData = new ArrayList<>();
    public ExcelDataListener(Long sourceId, byte sourceType)
    {
        this.sourceId = sourceId;
        this.sourceType = sourceType;
    }


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context){
        log.info("解析到表头：{}",headMap);
        //判断数据排列方式，纵向先处理，横向先存储所有数据，然后在遍历时最后处理。
        if(sourceType == 1)
        {
            parseVerticalHead(headMap);
        }
    }

    @Override
    //解析行数据
    public void invoke(Map<Integer, String> dataMap, AnalysisContext context) {
        log.info("解析到行数据：{}", dataMap);
        if(sourceType == 1)
        {
            int rowIndex = context.readRowHolder().getRowIndex() -1;
            parseVerticalData(dataMap, rowIndex);
        }else
        {
            allRowData.add(dataMap);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //如果时横向排列，则解析第一列作为表头，再按列解析数据。
        if(sourceType == 2){
            parseHorizontalHeadAndData();
        }

    }


//
//    解析表头
//


    // ------------------------ 纵向排列解析逻辑（原有逻辑） ------------------------
    private void parseVerticalHead(Map<Integer, String> headMap) {
        for(Map.Entry<Integer, String> entry : headMap.entrySet()){
            Integer colIndex = entry.getKey();
            String fieldName = entry.getValue().trim();
            if(fieldName.isEmpty())
            {
                continue;
            }
            String fieldCode = "field_" + colIndex;
            SourceField sourceField = new SourceField();
            sourceField.setSourceId(sourceId);
            sourceField.setFieldCode(fieldCode);
            sourceField.setFieldName(fieldName);
            sourceField.setFieldType("text");
            sourceField.setFieldOrder(colIndex);
            sourceField.setIsDeleted((byte)0);

            sourceFieldList.add(sourceField);
        }
    }

    private void parseVerticalData(Map<Integer,String>dataMap, int rowIndex) {
        for(Map.Entry<Integer, String> entry:dataMap.entrySet()) {
            Integer colIndex = entry.getKey();
            String dataValue = entry.getValue().trim();
            String fieldCode = "field_" + colIndex;

            for(SourceField field:sourceFieldList)
            {
                log.info("field:" + field.getFieldCode());
            }
            //跳过不存在的字段（比如数据列超过表头列数）
            if (sourceFieldList.stream().noneMatch(f -> f.getFieldCode().equals(fieldCode))){
                continue;
            }

            SourceData sourceData = new SourceData();
            sourceData.setSourceId(sourceId);
            sourceData.setRowIndex(rowIndex);
            sourceData.setColIndex(colIndex);
            sourceData.setFieldCode(fieldCode);
            sourceData.setDataValue(dataValue);
            sourceData.setIsDeleted((byte)0);
            sourceDataList.add(sourceData);

        }
    }
    // ------------------------ 横向排列解析逻辑（原有逻辑） ------------------------
    private void parseHorizontalHeadAndData(){
        if(allRowData.isEmpty())
        {
            log.warn("横向排列解析：无数行");
            return;
        }
        parseHorizontalHead();
        parseHorizontalData();

    }
    private void parseHorizontalHead() {

        for(int rowIndex = 0; rowIndex < allRowData.size(); rowIndex++)
        {
            Map<Integer, String> rowData = allRowData.get(rowIndex);
            //获取第一列（colindex = 0）的值作为字段值。
            String fieldName = rowData.get(0) == null?"":rowData.get(0).trim();
            if(fieldName.isEmpty())
            {
                continue;
            }
            String fieldCode = "field_" + rowIndex;

            SourceField sourceField = new SourceField();
            sourceField.setSourceId(sourceId);
            sourceField.setFieldCode(fieldCode);
            sourceField.setFieldName(fieldName);
            sourceField.setFieldType("text");
            sourceField.setFieldOrder(rowIndex);
            sourceField.setIsDeleted((byte)0);
            sourceFieldList.add(sourceField);

        }
    }

    public List<SourceField> getSourceFieldList() {
        return sourceFieldList;
    }

    public List<SourceData> getSourceDataList() {
        return sourceDataList;
    }

    private void parseHorizontalData(){
        //获取excel总列数（从第一行数据中获取最大索引）
        int maxColIndex = allRowData.get(0).keySet().stream().max(Integer::compareTo).orElse(0);

        //遍历每一列（列1开始，列0是表头）
        for(int colIndex = 1; colIndex <= maxColIndex; colIndex++)
        {
            //数据行索引，横向排列是一列代表一行数据。
            //通过存储逻辑数据行，访问时直接访问即可，利用rowIndex访问遍历数据条数，拆开fieldId取该字段值。
            int dataRowIndex = colIndex - 1;
            //遍历每一行（对应每一个字段）
            for(int fieldRowIndex = 0; fieldRowIndex < allRowData.size(); fieldRowIndex++)
            {
                Map<Integer, String> rowData = allRowData.get(fieldRowIndex);
                //获取当前列的单元格值
                String dataValue = rowData.get(colIndex) == null?"":rowData.get(colIndex).trim();
                //字段编码：field_字段行索引
                String fieldCode="field_" + fieldRowIndex;
                if(sourceFieldList.stream().noneMatch(f->f.getFieldCode().equals(fieldCode))){
                    continue;
                }
                SourceData sourceData = new SourceData();
                sourceData.setSourceId(sourceId);
                sourceData.setRowIndex(dataRowIndex);
                sourceData.setColIndex(colIndex);
                sourceData.setFieldCode(fieldCode);
                sourceData.setDataValue(dataValue);
                sourceData.setIsDeleted((byte) 0);
                sourceDataList.add(sourceData);
            }
        }
    }
}
