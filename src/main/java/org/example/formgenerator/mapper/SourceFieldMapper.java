package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.formgenerator.entity.SourceField;

import java.util.List;

/**
 * <p>
 * 数据源字段表 Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Mapper
public interface SourceFieldMapper extends BaseMapper<SourceField> {
    @Insert("<script>" +
            "INSERT INTO source_field (source_id, field_code, field_name, field_type, field_order, is_deleted) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','> " +
            "(#{item.sourceId}, #{item.fieldCode}, #{item.fieldName}, #{item.fieldType}, #{item.fieldOrder}, #{item.isDeleted}) " +
            "</foreach>" +
            "</script>")
    int batchInsertSourceField(@Param("list") List<SourceField> sourceFieldList);
}
