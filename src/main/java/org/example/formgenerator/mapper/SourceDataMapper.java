package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.formgenerator.entity.SourceData;

import java.util.List;

/**
 * <p>
 * 数据源数据表（存储原始行列数据） Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
@Mapper
public interface SourceDataMapper extends BaseMapper<SourceData> {
    // 批量插入source_data
    @Insert("<script>" +
            "INSERT INTO source_data (source_id, row_index, col_index, field_code, data_value, is_deleted) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','> " +
            "(#{item.sourceId}, #{item.rowIndex}, #{item.colIndex}, #{item.fieldCode}, #{item.dataValue}, #{item.isDeleted}) " +
            "</foreach>" +
            "</script>")
    int batchInsertSourceData(@Param("list") List<SourceData> sourceDataList);
}
