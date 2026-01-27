package org.example.formgenerator.service;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.entity.DataSource;
import org.example.formgenerator.entity.SourceData;
import org.example.formgenerator.entity.SourceField;
import org.example.formgenerator.listener.ExcelDataListener;
import org.example.formgenerator.mapper.DataSourceMapper;
import org.example.formgenerator.mapper.SourceDataMapper;
import org.example.formgenerator.mapper.SourceFieldMapper;
import org.example.formgenerator.utils.ApiException;
import org.example.formgenerator.utils.R;
import org.example.formgenerator.vo.ImportExcelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DataSourceService{
    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    SourceDataMapper sourceDataMapper;
    @Autowired
    SourceFieldMapper sourceFieldMapper;

    public String importExcelData(ImportExcelDTO importExcelDTO) throws ApiException, IOException {
        String originalFilename = importExcelDTO.getFile().getOriginalFilename();
        if(!StringUtils.hasText(originalFilename)||(!originalFilename.endsWith(".xlsx"))&&!originalFilename.endsWith(".xls")){
            throw new ApiException("请上传Excel格式文件（.xlsx/.xls）");
        }

        DataSource dataSource = new DataSource();
        dataSource.setSourceName(importExcelDTO.getSourceName());
        dataSource.setSourceType(importExcelDTO.getSourceType());
        dataSource.setSourceDesc(importExcelDTO.getSourceDesc()==null?"":importExcelDTO.getSourceDesc());
        dataSource.setDataCount(0);
        dataSource.setDataCount(0);
        dataSource.setCreator(importExcelDTO.getCreator());
        dataSource.setIsDeleted((byte)0);
        Long sourceId = dataSource.getId();
        ExcelDataListener listener = new ExcelDataListener(sourceId, importExcelDTO.getSourceType());
        EasyExcel.read(importExcelDTO.getFile().getInputStream(), listener)
                .headRowNumber(1)
                .sheet()
                .doRead();

        List<SourceField>sourceFieldList = listener.getSourceFieldList();
        dataSourceMapper.insert(dataSource);
        if(!sourceFieldList.isEmpty())
        {
            sourceFieldMapper.batchInsertSourceField(sourceFieldList);
        }
        List<SourceData> sourceDataList = listener.getSourceDataList();
        int dataCount = 0;
        if(!sourceDataList.isEmpty())
        {
            sourceDataMapper.batchInsertSourceData(sourceDataList);
            dataCount = sourceDataList.size()/sourceFieldList.size();
        }
        dataSource.setDataCount(dataCount);
        dataSourceMapper.updateById(dataSource);

        log.info("Excel导入成功，数据源ID：{}，字段数：{}，数据条数：{}", sourceId, sourceFieldList.size(), dataCount);
        return "导入成功！数据源ID：" + sourceId + "，共导入" + dataCount + "条数据";
    }
}
