package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
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
    /**
     * 通过sourceDataId（source_data主键）获取逻辑行号（已归一化的rowIndex）
     * @param sourceDataId source_data表主键ID
     * @return 逻辑行号（Integer，无有效数据返回null）
     */
    @Select("SELECT sd.row_index " +
            "FROM source_data sd " +
            "WHERE sd.id = #{sourceDataId} " +
            "  AND sd.is_deleted = 0") // 过滤逻辑删除数据
    Integer getLogicalRowIndexBySourceDataId(@Param("sourceDataId") Long sourceDataId);
}
