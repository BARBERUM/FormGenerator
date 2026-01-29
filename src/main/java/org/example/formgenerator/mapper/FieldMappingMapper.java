package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.formgenerator.entity.FieldMapping;

import java.util.List;

/**
 * <p>
 * 字段映射表（模板字段<->数据源字段） Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
public interface FieldMappingMapper extends BaseMapper<FieldMapping> {


    @Select("Select * from field_mapping Where template_id = #{templateId} And source_id = #{sourceId} And is_deleted = 0")
    public List<FieldMapping> selectByTemplateAndSource(@Param("templateId") Long templateId,
                                                        @Param("sourceId")Long sourceId);


    @Select("<script>" + // 动态SQL必须用<script>包裹，MyBatis才能解析
            "SELECT COUNT(*) FROM field_mapping " + // 关键字大写，空格分隔
            "WHERE is_deleted = 0 " + // 仅一次删除过滤，避免重复
            "AND template_id = #{templateId} " + // 每个条件单独换行+空格结尾
            "AND source_id = #{sourceId} " +
            "AND template_field_code = #{templateFieldCode} " + // 修正字段名为实际表字段
            "<if test='id != null'> AND id != #{id} </if>" + // 动态条件：编辑排除自身，前后加空格
            "</script>")
    int countUniqueMapping(
            @Param("templateId") Long templateId,
            @Param("sourceId") Long sourceId,
            @Param("templateFieldCode") String templateFieldCode,
            @Param("id") Long id // 与Service调用参数一致
    );


}
