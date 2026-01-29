package org.example.formgenerator.vo;

import lombok.Data;
import org.example.formgenerator.mapper.FormDataMapper;

import java.util.Map;

@Data
public class SourceRowDataVO {
    private Integer rowIndex; // source_data.id
    private Map<String, SourceDataVO> fieldDataMap;
}
