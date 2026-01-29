package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.formgenerator.entity.FormData;
import org.example.formgenerator.vo.SourceDataVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单数据明细表 Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
public interface FormDataMapper extends BaseMapper<FormData> {
    @Select("SELECT sd.id, sd.field_code, sd.data_value FROM source_data sd " +
            "LEFT JOIN source_field sf ON sd.source_id = sf.source_id AND sd.field_code = sf.field_code " +
            "WHERE sd.source_id = #{sourceId} AND sd.is_deleted = 0 AND sf.is_deleted = 0 " +
            "ORDER BY sd.row_index ASC, sd.col_index ASC")
    List<SourceDataVO> selectSourceDataByCode(@Param("sourceId") Long sourceId);
    // 数据源数据VO（仅用于查询）


    // 其他原有方法（如selectSourceDataByCode、insert等）保留不变
    /**
     * 批量插入表单数据
     * @param formDataList 表单数据列表（包含logicalDataRow、fieldCode等字段）
     * @return 插入成功的条数
     */
    int batchInsertFormData(@Param("list") List<FormData> formDataList);


    /**
     * 根据表单ID查询未删除的表单数据明细
     * @param formId 表单实例ID
     * @return 表单数据列表（按逻辑行号+字段编码排序，保证展示顺序）
     */
    @Select("SELECT id, form_id, field_code, field_value, source_data_id " +
            "FROM form_data " +
            "WHERE form_id = #{formId} " +
            "  AND is_deleted = 0 " +
            "ORDER BY field_code ASC")
    List<FormData> selectFormDataByFormId(@Param("formId") Long formId);


    @Update("Update form_data set is_deleted = 1 where form_id = #{formId}")
    void softDeleteFromDataByFormId(@Param("formId") Long formId);
}
