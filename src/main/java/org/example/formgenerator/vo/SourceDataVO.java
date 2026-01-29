package org.example.formgenerator.vo;

import lombok.Data;

@Data
public class SourceDataVO {
    private Long id; // source_data.id
    private String fieldCode;
    private String dataValue;
}